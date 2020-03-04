package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.PermissaoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.PermissaoModel;
import com.joaopauloschmitz.algafoodapi.api.openapi.controller.PermissaoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.Permissao;
import com.joaopauloschmitz.algafoodapi.domain.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissaoController implements PermissaoControllerOpenApi {

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private PermissaoModelAssembler permissaoModelAssembler;

    @Override
    @GetMapping
    public CollectionModel<PermissaoModel> listar() {
        List<Permissao> permissoes = this.permissaoRepository.findAll();
        return this.permissaoModelAssembler.toCollectionModel(permissoes);
    }
}
