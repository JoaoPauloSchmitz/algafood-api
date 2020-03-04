package com.joaopauloschmitz.algafoodapi.api.assembler;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.controller.RestauranteProdutoController;
import com.joaopauloschmitz.algafoodapi.api.model.ProdutoModel;
import com.joaopauloschmitz.algafoodapi.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoModelAssembler extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public ProdutoModelAssembler() {
        super(RestauranteProdutoController.class, ProdutoModel.class);
    }

    public ProdutoModel toModel(Produto produto) {
        ProdutoModel produtoModel = createModelWithId(produto.getId(), produto, produto.getRestaurante().getId());
        this.modelMapper.map(produto, produtoModel);

        produtoModel.add(this.algaLinks.linkToProdutos(produto.getRestaurante().getId(), "produtos"));
        produtoModel.add(this.algaLinks.linkToFotoProduto(produto.getRestaurante().getId(), produto.getId(), "foto"));

        return produtoModel;
    }

}
