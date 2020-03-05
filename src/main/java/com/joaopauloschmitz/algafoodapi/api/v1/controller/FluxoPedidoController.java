package com.joaopauloschmitz.algafoodapi.api.v1.controller;

import com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller.FluxoPedidoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.service.FluxoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/pedidos/{codigoPedido}", produces = MediaType.APPLICATION_JSON_VALUE)
public class FluxoPedidoController implements FluxoPedidoControllerOpenApi {

    @Autowired
    private FluxoPedidoService fluxoPedidoService;

    @PutMapping("/confirmacao")
    public ResponseEntity<Void> confirmar(@PathVariable String codigoPedido) {
        this.fluxoPedidoService.confirmar(codigoPedido);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/entrega")
    public ResponseEntity<Void> entregar(@PathVariable String codigoPedido) {
        this.fluxoPedidoService.entregar(codigoPedido);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancelamento")
    public ResponseEntity<Void> cancelar(@PathVariable String codigoPedido) {
        this.fluxoPedidoService.cancelar(codigoPedido);
        return ResponseEntity.noContent().build();
    }
}
