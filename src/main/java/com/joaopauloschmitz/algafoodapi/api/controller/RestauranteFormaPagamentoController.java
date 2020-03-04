package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.assembler.FormaPagamentoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.FormaPagamentoModel;
import com.joaopauloschmitz.algafoodapi.api.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteFormaPagamentoController implements RestauranteFormaPagamentoControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @Autowired
    private AlgaLinks algaLinks;

    @Override
    @GetMapping
    public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = this.cadastroRestauranteService.buscarOuFalhar(restauranteId);

        CollectionModel<FormaPagamentoModel> formaPagamentoModels = this.formaPagamentoModelAssembler
                .toCollectionModel(restaurante.getFormasPagamento())
                .removeLinks()
                .add(this.algaLinks.linkToRestauranteFormasPagamento(restauranteId))
                .add(this.algaLinks.linkToRestauranteFormaPagamentoAssociacao(restauranteId, "associar"));

        formaPagamentoModels.getContent().forEach(formaPagamentoModel -> {
            formaPagamentoModel.add(this.algaLinks
             .linkToRestauranteFormaPagamentoDesassociacao(restauranteId, formaPagamentoModel.getId(), "desassociar"));
        });

        return formaPagamentoModels;
    }

    @Override
    @DeleteMapping("/{fomaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long fomaPagamentoId) {
        this.cadastroRestauranteService.desassociarFormaPagamento(restauranteId, fomaPagamentoId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{fomaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long fomaPagamentoId) {
        this.cadastroRestauranteService.associarFormaPagamento(restauranteId, fomaPagamentoId);
        return ResponseEntity.noContent().build();
    }
}
