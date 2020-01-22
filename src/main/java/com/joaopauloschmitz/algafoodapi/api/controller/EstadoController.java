package com.joaopauloschmitz.algafoodapi.api.controller;

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

    @GetMapping
    public List<Estado> listar() {
        return estadoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Estado buscar(@PathVariable Long id) {
        return this.cadastroEstadoService.buscarOuFalhar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estado adicionar(@RequestBody @Valid Estado estado) {
        return cadastroEstadoService.salvar(estado);
    }

    @PutMapping("/{id}")
    public Estado atualizar(@PathVariable Long id,
                                            @RequestBody @Valid Estado estado) {

        Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(id);
        BeanUtils.copyProperties(estado, estadoAtual, "id");

        return cadastroEstadoService.salvar(estadoAtual);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
       this.cadastroEstadoService.excluir(id);
    }
}
