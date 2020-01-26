package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.CidadeInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.CidadeModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.CidadeModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.CidadeInput;
import com.joaopauloschmitz.algafoodapi.domain.exception.EstadoNaoEncontradoException;
import com.joaopauloschmitz.algafoodapi.domain.exception.NegocioException;
import com.joaopauloschmitz.algafoodapi.domain.model.Cidade;
import com.joaopauloschmitz.algafoodapi.domain.repository.CidadeRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @Autowired
    private CidadeModelAssembler cidadeModelAssembler;

    @Autowired
    private CidadeInputDiassembler cidadeInputDiassembler;

    @GetMapping
    public List<CidadeModel> listar() {
        return this.cidadeModelAssembler.toCollectionModel(this.cidadeRepository.findAll());
    }

    @GetMapping("/{id}")
    public CidadeModel buscar(@PathVariable Long id) {
        Cidade cidade = this.cadastroCidadeService.buscarOuFalhar(id);
        return this.cidadeModelAssembler.toModel(cidade);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidade = this.cidadeInputDiassembler.toDomainObject(cidadeInput);
            return this.cidadeModelAssembler.toModel(this.cadastroCidadeService.salvar(cidade));
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public CidadeModel atualizar(@PathVariable Long id,
                                       @RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidadeAtual = this.cadastroCidadeService.buscarOuFalhar(id);
            this.cidadeInputDiassembler.copyToDomainObject(cidadeInput, cidadeAtual);

            return this.cidadeModelAssembler.toModel(this.cadastroCidadeService.salvar(cidadeAtual));
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        this.cadastroCidadeService.excluir(id);
    }
}
