package com.joaopauloschmitz.algafoodapi.domain.listener;

import com.joaopauloschmitz.algafoodapi.domain.event.PedidoConfirmadoEvent;
import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import com.joaopauloschmitz.algafoodapi.domain.service.EnvioEmailService;
import com.joaopauloschmitz.algafoodapi.domain.service.EnvioEmailService.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoClientePedidoConfirmadoListener {

    @Autowired
    private EnvioEmailService envioEmailService;

    @TransactionalEventListener
    public void aoConfirmarPedido(PedidoConfirmadoEvent pedidoConfirmadoEvent) {

        Pedido pedido = pedidoConfirmadoEvent.getPedido();

        var mensagem = Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
                .corpo("emails/pedido-confirmado.html")
                .variavel("pedido", pedido)
                .destinatario(pedido.getCliente().getEmail())
                .build();

        this.envioEmailService.enviar(mensagem);
    }
}
