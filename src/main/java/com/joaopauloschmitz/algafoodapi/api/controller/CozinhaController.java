package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.CozinhaInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.CozinhaModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.CozinhaModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.CozinhaInput;
import com.joaopauloschmitz.algafoodapi.api.openapi.controller.CozinhaControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.Cozinha;
import com.joaopauloschmitz.algafoodapi.domain.repository.CozinhaRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroCozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController implements CozinhaControllerOpenApi {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Autowired
    private CozinhaModelAssembler cozinhaModelAssembler;

    @Autowired
    private CozinhaInputDiassembler cozinhaInputDiassembler;

    @GetMapping
    public Page<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
        Page<Cozinha> cozinhasPage = this.cozinhaRepository.findAll(pageable);

        List<CozinhaModel> cozinhasModel = this.cozinhaModelAssembler.toCollectionModel(cozinhasPage.getContent());

        Page<CozinhaModel> cozinhasModelPage = new PageImpl<>(cozinhasModel, pageable,
                cozinhasPage.getTotalElements());

        return cozinhasModelPage;
    }

    @GetMapping("/{id}")
    public CozinhaModel buscar(@PathVariable Long id) {
        Cozinha cozinha = this.cadastroCozinhaService.buscarOuFalhar(id);
        return this.cozinhaModelAssembler.toModel(cozinha);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
        Cozinha cozinha = this.cozinhaInputDiassembler.toDomainObject(cozinhaInput);
        return this.cozinhaModelAssembler.toModel(this.cadastroCozinhaService.salvar(cozinha));
    }

    @PutMapping("/{id}")
    public CozinhaModel atualizar(@PathVariable Long id, @RequestBody @Valid CozinhaInput cozinhaInput) {

        Cozinha cozinhaAtual = this.cadastroCozinhaService.buscarOuFalhar(id);
        this.cozinhaInputDiassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);

        return this.cozinhaModelAssembler.toModel(this.cadastroCozinhaService.salvar(cozinhaAtual));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        this.cadastroCozinhaService.excluir(id);
    }
}
