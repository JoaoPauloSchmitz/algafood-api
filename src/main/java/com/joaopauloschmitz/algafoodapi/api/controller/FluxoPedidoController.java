package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.domain.service.FluxoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos/{codigoPedido}")
public class FluxoPedidoController {

    @Autowired
    private FluxoPedidoService fluxoPedidoService;

    @PutMapping("/confirmacao")
    public void confirmar(@PathVariable String codigoPedido) {
        this.fluxoPedidoService.confirmar(codigoPedido);
    }

    @PutMapping("/entrega")
    public void entregar(@PathVariable String codigoPedido) {
        this.fluxoPedidoService.entregar(codigoPedido);
    }

    @PutMapping("/cancelamento")
    public void cancelar(@PathVariable String codigoPedido) {
        this.fluxoPedidoService.cancelar(codigoPedido);
    }
}
