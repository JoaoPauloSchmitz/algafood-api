package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.EstadoInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.EstadoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.EstadoModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.EstadoInput;
import com.joaopauloschmitz.algafoodapi.domain.model.Estado;
import com.joaopauloschmitz.algafoodapi.domain.repository.EstadoRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @Autowired
    private EstadoModelAssembler estadoModelAssembler;

    @Autowired
    private EstadoInputDiassembler estadoInputDiassembler;

    @GetMapping
    public List<EstadoModel> listar() {
        return this.estadoModelAssembler.toCollectionModel(this.estadoRepository.findAll());
    }

    @GetMapping("/{id}")
    public EstadoModel buscar(@PathVariable Long id) {
        Estado estado = this.cadastroEstadoService.buscarOuFalhar(id);
        return this.estadoModelAssembler.toModel(estado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
        Estado estado = this.estadoInputDiassembler.toDomainObject(estadoInput);
        return this.estadoModelAssembler.toModel(this.cadastroEstadoService.salvar(estado));
    }

    @PutMapping("/{id}")
    public EstadoModel atualizar(@PathVariable Long id,
                                            @RequestBody @Valid EstadoInput estadoInput) {

        Estado estadoAtual = this.cadastroEstadoService.buscarOuFalhar(id);
        this.estadoInputDiassembler.copyToDomainObject(estadoInput, estadoAtual);

        return this.estadoModelAssembler.toModel(cadastroEstadoService.salvar(estadoAtual));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
       this.cadastroEstadoService.excluir(id);
    }
}
