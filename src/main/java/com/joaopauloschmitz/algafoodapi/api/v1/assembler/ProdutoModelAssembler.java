package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.v1.controller.RestauranteProdutoController;
import com.joaopauloschmitz.algafoodapi.api.v1.model.ProdutoModel;
import com.joaopauloschmitz.algafoodapi.core.security.AlgaSecurity;
import com.joaopauloschmitz.algafoodapi.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ProdutoModelAssembler extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public ProdutoModelAssembler() {
        super(RestauranteProdutoController.class, ProdutoModel.class);
    }

    public ProdutoModel toModel(Produto produto) {
        ProdutoModel produtoModel = createModelWithId(produto.getId(), produto, produto.getRestaurante().getId());
        this.modelMapper.map(produto, produtoModel);

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            produtoModel.add(this.algaLinks.linkToProdutos(produto.getRestaurante().getId(), "produtos"));
            produtoModel.add(this.algaLinks.linkToFotoProduto(produto.getRestaurante().getId(), produto.getId(), "foto"));
        }

        return produtoModel;
    }

}
