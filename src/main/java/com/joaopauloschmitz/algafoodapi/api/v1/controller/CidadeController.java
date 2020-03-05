package com.joaopauloschmitz.algafoodapi.api.v1.controller;

import com.joaopauloschmitz.algafoodapi.api.ResourceUriHelper;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.CidadeInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.CidadeModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.v1.model.CidadeModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.CidadeInput;
import com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller.CidadeControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.exception.EstadoNaoEncontradoException;
import com.joaopauloschmitz.algafoodapi.domain.exception.NegocioException;
import com.joaopauloschmitz.algafoodapi.domain.model.Cidade;
import com.joaopauloschmitz.algafoodapi.domain.repository.CidadeRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController implements CidadeControllerOpenApi {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @Autowired
    private CidadeModelAssembler cidadeModelAssembler;

    @Autowired
    private CidadeInputDiassembler cidadeInputDiassembler;

    @Override
    @GetMapping
    public CollectionModel<CidadeModel> listar() {
        return this.cidadeModelAssembler.toCollectionModel(this.cidadeRepository.findAll());
    }

    @Override
    @GetMapping("/{id}")
    public CidadeModel buscar(@PathVariable Long id) {
        Cidade cidade = this.cadastroCidadeService.buscarOuFalhar(id);
        return this.cidadeModelAssembler.toModel(cidade);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidade = this.cidadeInputDiassembler.toDomainObject(cidadeInput);

            ResourceUriHelper.addUriInResponseHeader(cidade.getId());

            return this.cidadeModelAssembler.toModel(this.cadastroCidadeService.salvar(cidade));
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @Override
    @PutMapping("/{id}")
    public CidadeModel atualizar(@PathVariable Long id, @RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidadeAtual = this.cadastroCidadeService.buscarOuFalhar(id);
            this.cidadeInputDiassembler.copyToDomainObject(cidadeInput, cidadeAtual);

            return this.cidadeModelAssembler.toModel(this.cadastroCidadeService.salvar(cidadeAtual));
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        this.cadastroCidadeService.excluir(id);
    }
}
