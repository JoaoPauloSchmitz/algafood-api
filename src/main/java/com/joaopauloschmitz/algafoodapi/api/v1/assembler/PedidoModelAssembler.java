package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.v1.controller.PedidoController;
import com.joaopauloschmitz.algafoodapi.api.v1.model.PedidoModel;
import com.joaopauloschmitz.algafoodapi.core.security.AlgaSecurity;
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

    @Autowired
    private AlgaSecurity algaSecurity;

    public PedidoModelAssembler() {
        super(PedidoController.class, PedidoModel.class);
    }

    @Override
    public PedidoModel toModel(Pedido pedido) {
        PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        this.modelMapper.map(pedido, pedidoModel);

        pedidoModel.add(this.algaLinks.linkToPedidos("pedidos"));

        if (this.algaSecurity.podeGerenciarPedidos(pedido.getCodigo())) {

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
        }

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            pedidoModel.getRestaurante().add(
                    this.algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
        }

        if (this.algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
            pedidoModel.getCliente().add(
                    this.algaLinks.linkToUsuario(pedido.getCliente().getId()));
        }

        if (this.algaSecurity.podeConsultarFormasPagamento()) {
            pedidoModel.getFormaPagamento().add(
                    this.algaLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));
        }

        if (this.algaSecurity.podeConsultarCidades()) {
            pedidoModel.getEnderecoEntrega().getCidade().add(
                    this.algaLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));
        }

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            pedidoModel.getItens().forEach(item -> {
                item.add(this.algaLinks.linkToProduto(
                        pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto"));
            });
        }

        return pedidoModel;
    }


}
