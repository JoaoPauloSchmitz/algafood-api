package com.joaopauloschmitz.algafoodapi.domain.event;

import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedidoCanceladoEvent {

    private Pedido pedido;
}
