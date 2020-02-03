package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.domain.service.FluxoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos/{id}")
public class FluxoPedidoController {

    @Autowired
    private FluxoPedidoService fluxoPedidoService;

    @PutMapping("/confirmacao")
    public void confirmar(@PathVariable Long id) {
        this.fluxoPedidoService.confirmar(id);
    }

    @PutMapping("/entrega")
    public void entregar(@PathVariable Long id) {
        this.fluxoPedidoService.entregar(id);
    }

    @PutMapping("/cancelamento")
    public void cancelar(@PathVariable Long id) {
        this.fluxoPedidoService.cancelar(id);
    }
}
