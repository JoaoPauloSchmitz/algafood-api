package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.domain.model.Cozinha;
import com.joaopauloschmitz.algafoodapi.domain.repository.CozinhaRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @GetMapping
    public List<Cozinha> listar() {
        return this.cozinhaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cozinha buscar(@PathVariable Long id) {
        return this.cadastroCozinhaService.buscarOuFalhar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha adicionar(@RequestBody @Valid Cozinha cozinha) {
       return this.cadastroCozinhaService.salvar(cozinha);
    }

    @PutMapping("/{id}")
    public Cozinha atualizar(@PathVariable Long id, @RequestBody @Valid Cozinha cozinha) {

        Cozinha cozinhaAtual = this.cadastroCozinhaService.buscarOuFalhar(id);
        BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");

        return this.cadastroCozinhaService.salvar(cozinhaAtual);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        this.cadastroCozinhaService.excluir(id);
    }
}
