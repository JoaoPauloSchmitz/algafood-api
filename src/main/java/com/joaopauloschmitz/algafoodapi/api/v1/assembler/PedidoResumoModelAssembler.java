package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.v1.controller.PedidoController;
import com.joaopauloschmitz.algafoodapi.api.v1.model.PedidoResumoModel;
import com.joaopauloschmitz.algafoodapi.core.security.AlgaSecurity;
import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class PedidoResumoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public PedidoResumoModelAssembler() {
        super(PedidoController.class, PedidoResumoModel.class);
    }

    @Override
    public PedidoResumoModel toModel(Pedido pedido) {
        PedidoResumoModel pedidoResumoModel = createModelWithId(pedido.getId(), pedido);
        this.modelMapper.map(pedido, pedidoResumoModel);

        if (this.algaSecurity.podePesquisarPedidos()) {
            pedidoResumoModel.add(this.algaLinks.linkToPedidos("pedidos"));
        }

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            pedidoResumoModel.getRestaurante().add(
                    this.algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
        }

        if (this.algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
            pedidoResumoModel.getCliente().add(this.algaLinks.linkToUsuario(pedido.getCliente().getId()));
        }

        return pedidoResumoModel;
    }

}
