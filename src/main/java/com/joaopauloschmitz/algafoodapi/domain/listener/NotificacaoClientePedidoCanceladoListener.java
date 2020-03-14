package com.joaopauloschmitz.algafoodapi.domain.listener;

import com.joaopauloschmitz.algafoodapi.domain.event.PedidoCanceladoEvent;
import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import com.joaopauloschmitz.algafoodapi.domain.service.EnvioEmailService;
import com.joaopauloschmitz.algafoodapi.domain.service.EnvioEmailService.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoClientePedidoCanceladoListener {

    @Autowired
    private EnvioEmailService envioEmailService;

    @TransactionalEventListener
    public void aoCancelarPedido(PedidoCanceladoEvent pedidoCanceladoEvent) {

        Pedido pedido = pedidoCanceladoEvent.getPedido();

        var mensagem = Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + " - Pedido cancelado")
                .corpo("emails/pedido-cancelado.html")
                .variavel("pedido", pedido)
                .destinatario(pedido.getCliente().getEmail())
                .build();

        this.envioEmailService.enviar(mensagem);
    }
}
