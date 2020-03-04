package com.joaopauloschmitz.algafoodapi.api.assembler;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.controller.PedidoController;
import com.joaopauloschmitz.algafoodapi.api.controller.RestauranteController;
import com.joaopauloschmitz.algafoodapi.api.controller.UsuarioController;
import com.joaopauloschmitz.algafoodapi.api.model.PedidoResumoModel;
import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoResumoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public PedidoResumoModelAssembler() {
        super(PedidoController.class, PedidoResumoModel.class);
    }

    @Override
    public PedidoResumoModel toModel(Pedido pedido) {
        PedidoResumoModel pedidoResumoModel = createModelWithId(pedido.getId(), pedido);
        this.modelMapper.map(pedido, pedidoResumoModel);

        pedidoResumoModel.add(this.algaLinks.linkToPedidos("pedidos"));

        pedidoResumoModel.getRestaurante().add(
                this.algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));

        pedidoResumoModel.getCliente().add(this.algaLinks.linkToUsuario(pedido.getCliente().getId()));

        return pedidoResumoModel;
    }

}
