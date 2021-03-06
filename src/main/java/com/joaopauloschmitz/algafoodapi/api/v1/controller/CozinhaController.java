package com.joaopauloschmitz.algafoodapi.api.v1.controller;

import com.joaopauloschmitz.algafoodapi.api.v1.assembler.CozinhaInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.CozinhaModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.v1.model.CozinhaModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.CozinhaInput;
import com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.core.security.CheckSecurity;
import com.joaopauloschmitz.algafoodapi.domain.model.Cozinha;
import com.joaopauloschmitz.algafoodapi.domain.repository.CozinhaRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroCozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController implements CozinhaControllerOpenApi {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Autowired
    private CozinhaModelAssembler cozinhaModelAssembler;

    @Autowired
    private CozinhaInputDiassembler cozinhaInputDiassembler;

    @Autowired
    private PagedResourcesAssembler<Cozinha> cozinhaPagedResourcesAssembler;

    @CheckSecurity.Cozinhas.PodeConsultar
    @Override
    @GetMapping
    public PagedModel<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
        Page<Cozinha> cozinhasPage = this.cozinhaRepository.findAll(pageable);

        PagedModel<CozinhaModel> cozinhaModels = this.cozinhaPagedResourcesAssembler
                .toModel(cozinhasPage, cozinhaModelAssembler);

        return cozinhaModels;
    }

    @CheckSecurity.Cozinhas.PodeConsultar
    @Override
    @GetMapping("/{id}")
    public CozinhaModel buscar(@PathVariable Long id) {
        Cozinha cozinha = this.cadastroCozinhaService.buscarOuFalhar(id);
        return this.cozinhaModelAssembler.toModel(cozinha);
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
        Cozinha cozinha = this.cozinhaInputDiassembler.toDomainObject(cozinhaInput);
        return this.cozinhaModelAssembler.toModel(this.cadastroCozinhaService.salvar(cozinha));
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @Override
    @PutMapping("/{id}")
    public CozinhaModel atualizar(@PathVariable Long id, @RequestBody @Valid CozinhaInput cozinhaInput) {

        Cozinha cozinhaAtual = this.cadastroCozinhaService.buscarOuFalhar(id);
        this.cozinhaInputDiassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);

        return this.cozinhaModelAssembler.toModel(this.cadastroCozinhaService.salvar(cozinhaAtual));

    }

    @CheckSecurity.Cozinhas.PodeEditar
    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        this.cadastroCozinhaService.excluir(id);
    }
}
