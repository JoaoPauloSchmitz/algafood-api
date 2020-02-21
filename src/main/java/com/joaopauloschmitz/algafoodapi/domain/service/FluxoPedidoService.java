package com.joaopauloschmitz.algafoodapi.domain.service;

import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import com.joaopauloschmitz.algafoodapi.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FluxoPedidoService {

    @Autowired
    private EmissaoPedidoService emissaoPedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public void confirmar(String codigoPedido) {
        Pedido pedido = this.emissaoPedidoService.buscarOuFalhar(codigoPedido);
        pedido.confirmar();

        this.pedidoRepository.save(pedido);
    }

    @Transactional
    public void entregar(String codigoPedido) {
        Pedido pedido = this.emissaoPedidoService.buscarOuFalhar(codigoPedido);
        pedido.entregar();
    }

    @Transactional
    public void cancelar(String codigoPedido) {
        Pedido pedido = this.emissaoPedidoService.buscarOuFalhar(codigoPedido);
        pedido.cancelar();

        this.pedidoRepository.save(pedido);
    }
}
