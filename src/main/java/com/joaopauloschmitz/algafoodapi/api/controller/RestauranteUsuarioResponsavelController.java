package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.assembler.UsuarioModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.UsuarioModel;
import com.joaopauloschmitz.algafoodapi.api.openapi.controller.RestauranteUsuarioResponsavelControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/responsaveis", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteUsuarioResponsavelController implements RestauranteUsuarioResponsavelControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    private AlgaLinks algaLinks;

    @Override
    @GetMapping
    public CollectionModel<UsuarioModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = this.cadastroRestauranteService.buscarOuFalhar(restauranteId);

        CollectionModel<UsuarioModel> usuarioModels = this.usuarioModelAssembler
                .toCollectionModel(restaurante.getResponsaveis())
                .removeLinks()
                .add(this.algaLinks.linkToResponsaveisRestaurante(restauranteId))
                .add(this.algaLinks.linkToRestauranteResponsavelAssociacao(restauranteId, "associar"));

        usuarioModels.getContent().stream().forEach(usuarioModel -> {
            usuarioModel.add(this.algaLinks.linkToRestauranteResponsavelDesassociacao(
                    restauranteId, usuarioModel.getId(), "desassociar"));
        });

        return usuarioModels;
    }

    @Override
    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        this.cadastroRestauranteService.desassociarResponsavel(restauranteId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        this.cadastroRestauranteService.associarResponsavel(restauranteId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
