package com.joaopauloschmitz.algafoodapi.api.assembler;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.controller.RestauranteProdutoFotoController;
import com.joaopauloschmitz.algafoodapi.api.model.FotoProdutoModel;
import com.joaopauloschmitz.algafoodapi.domain.model.FotoProduto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class FotoProdutoModelAssembler extends RepresentationModelAssemblerSupport<FotoProduto, FotoProdutoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public FotoProdutoModelAssembler() {
        super(RestauranteProdutoFotoController.class, FotoProdutoModel.class);
    }

    @Override
    public FotoProdutoModel toModel(FotoProduto fotoProduto) {
        FotoProdutoModel fotoProdutoModel = this.modelMapper.map(fotoProduto, FotoProdutoModel.class);

        fotoProdutoModel.add(this.algaLinks.linkToFotoProduto(fotoProduto.getRestauranteId(), fotoProduto.getId()));
        fotoProdutoModel.add(this.algaLinks.linkToProduto(fotoProduto.getRestauranteId(), fotoProduto.getId(), "produto"));

        return fotoProdutoModel;
    }

}
