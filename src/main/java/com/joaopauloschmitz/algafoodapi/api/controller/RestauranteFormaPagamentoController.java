package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.FormaPagamentoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.FormaPagamentoModel;
import com.joaopauloschmitz.algafoodapi.api.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteFormaPagamentoController implements RestauranteFormaPagamentoControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @GetMapping
    public List<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = this.cadastroRestauranteService.buscarOuFalhar(restauranteId);
        return this.formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento());
    }

    @DeleteMapping("/{fomaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociar(@PathVariable Long restauranteId, @PathVariable Long fomaPagamentoId) {
        this.cadastroRestauranteService.desassociarFormaPagamento(restauranteId, fomaPagamentoId);
    }

    @PutMapping("/{fomaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associar(@PathVariable Long restauranteId, @PathVariable Long fomaPagamentoId) {
        this.cadastroRestauranteService.associarFormaPagamento(restauranteId, fomaPagamentoId);
    }
}
