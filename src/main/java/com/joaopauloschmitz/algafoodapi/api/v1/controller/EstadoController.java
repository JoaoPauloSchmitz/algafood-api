package com.joaopauloschmitz.algafoodapi.api.v1.controller;

import com.joaopauloschmitz.algafoodapi.api.v1.assembler.EstadoInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.EstadoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.v1.model.EstadoModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.EstadoInput;
import com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller.EstadoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.Estado;
import com.joaopauloschmitz.algafoodapi.domain.repository.EstadoRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/estados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadoController implements EstadoControllerOpenApi {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @Autowired
    private EstadoModelAssembler estadoModelAssembler;

    @Autowired
    private EstadoInputDiassembler estadoInputDiassembler;

    @Override
    @GetMapping
    public CollectionModel<EstadoModel> listar() {
        List<Estado> estados = this.estadoRepository.findAll();
        return this.estadoModelAssembler.toCollectionModel(estados);
    }

    @Override
    @GetMapping("/{id}")
    public EstadoModel buscar(@PathVariable Long id) {
        Estado estado = this.cadastroEstadoService.buscarOuFalhar(id);
        return this.estadoModelAssembler.toModel(estado);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
        Estado estado = this.estadoInputDiassembler.toDomainObject(estadoInput);
        return this.estadoModelAssembler.toModel(this.cadastroEstadoService.salvar(estado));
    }

    @Override
    @PutMapping("/{id}")
    public EstadoModel atualizar(@PathVariable Long id,
                                            @RequestBody @Valid EstadoInput estadoInput) {

        Estado estadoAtual = this.cadastroEstadoService.buscarOuFalhar(id);
        this.estadoInputDiassembler.copyToDomainObject(estadoInput, estadoAtual);

        return this.estadoModelAssembler.toModel(cadastroEstadoService.salvar(estadoAtual));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
       this.cadastroEstadoService.excluir(id);
    }
}
