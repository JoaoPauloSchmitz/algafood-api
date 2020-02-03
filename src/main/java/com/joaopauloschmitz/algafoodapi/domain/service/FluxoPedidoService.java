package com.joaopauloschmitz.algafoodapi.domain.service;

import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FluxoPedidoService {

    @Autowired
    private EmissaoPedidoService emissaoPedidoService;

    @Transactional
    public void confirmar(Long id) {
        Pedido pedido = this.emissaoPedidoService.buscarOuFalhar(id);
        pedido.confirmar();
    }

    @Transactional
    public void entregar(Long id) {
        Pedido pedido = this.emissaoPedidoService.buscarOuFalhar(id);
        pedido.entregar();
    }

    @Transactional
    public void cancelar(Long id) {
        Pedido pedido = this.emissaoPedidoService.buscarOuFalhar(id);
        pedido.cancelar();
    }
}
