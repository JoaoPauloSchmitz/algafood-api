package com.joaopauloschmitz.algafoodapi.api.assembler;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.controller.PedidoController;
import com.joaopauloschmitz.algafoodapi.api.model.PedidoModel;
import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public PedidoModelAssembler() {
        super(PedidoController.class, PedidoModel.class);
    }

    @Override
    public PedidoModel toModel(Pedido pedido) {
        PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        this.modelMapper.map(pedido, pedidoModel);

        pedidoModel.add(this.algaLinks.linkToPedidos("pedidos"));

        if (pedido.podeSerConfirmado()) {
            pedidoModel.add(
                    this.algaLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
        }

        if (pedido.podeSerCancelado()) {
            pedidoModel.add(
                    this.algaLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
        }

        if (pedido.podeSerEntregue()) {
            pedidoModel.add(
                    this.algaLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
        }

        pedidoModel.getRestaurante().add(
                this.algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));

        pedidoModel.getCliente().add(
                this.algaLinks.linkToUsuario(pedido.getCliente().getId()));

        pedidoModel.getFormaPagamento().add(
                this.algaLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));

        pedidoModel.getEnderecoEntrega().getCidade().add(
                this.algaLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));

        pedidoModel.getItens().forEach(item -> {
            item.add(this.algaLinks.linkToProduto(
                    pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto"));
        });

        return pedidoModel;
    }


}
