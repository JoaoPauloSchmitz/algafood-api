package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RootEntryPointController {

    @Autowired
    private AlgaLinks algaLinks;

    @GetMapping
    public RootEntryPointModel root() {
        var rootEntryPointModel = new RootEntryPointModel();

        rootEntryPointModel.add(this.algaLinks.linkToCozinhas("cozinhas"));
        rootEntryPointModel.add(this.algaLinks.linkToPedidos("pedidos"));
        rootEntryPointModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
        rootEntryPointModel.add(this.algaLinks.linkToGrupos("grupos"));
        rootEntryPointModel.add(this.algaLinks.linkToUsuarios("usuarios"));
        rootEntryPointModel.add(this.algaLinks.linkToPermissoes("permissoes"));
        rootEntryPointModel.add(this.algaLinks.linkToFormasPagamento("formas-pagamento"));
        rootEntryPointModel.add(this.algaLinks.linkToEstados("estados"));
        rootEntryPointModel.add(this.algaLinks.linkToCidades("cidades"));
        rootEntryPointModel.add(this.algaLinks.linkToEstatisticas("estatisticas"));

        return rootEntryPointModel;
    }

    private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel> {
    }
}
