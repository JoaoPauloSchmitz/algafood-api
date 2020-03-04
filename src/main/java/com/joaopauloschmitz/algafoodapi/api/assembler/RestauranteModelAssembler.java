package com.joaopauloschmitz.algafoodapi.api.assembler;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.controller.RestauranteController;
import com.joaopauloschmitz.algafoodapi.api.model.RestauranteModel;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestauranteModelAssembler extends
        RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        this.modelMapper.map(restaurante, restauranteModel);

        restauranteModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));

        if (restaurante.ativacaoPermitida()) {
            restauranteModel.add(
                    this.algaLinks.linkToRestauranteAtivacao(restaurante.getId(), "ativar"));
        }

        if (restaurante.inativacaoPermitida()) {
            restauranteModel.add(
                    this.algaLinks.linkToRestauranteInativacao(restaurante.getId(), "inativar"));
        }

        if (restaurante.aberturaPermitida()) {
            restauranteModel.add(
                    this.algaLinks.linkToRestauranteAbertura(restaurante.getId(), "abrir"));
        }

        if (restaurante.fechamentoPermitido()) {
            restauranteModel.add(
                    this.algaLinks.linkToRestauranteFechamento(restaurante.getId(), "fechar"));
        }

        restauranteModel.add(this.algaLinks.linkToProdutos(restaurante.getId(), "produtos"));

        restauranteModel.getCozinha().add(
                this.algaLinks.linkToCozinha(restaurante.getCozinha().getId()));

        if (restauranteModel.getEndereco() != null && restauranteModel.getEndereco().getCidade() != null) {
            restauranteModel.getEndereco().getCidade().add(
                    this.algaLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
        }

        restauranteModel.add(this.algaLinks.linkToRestauranteFormasPagamento(restaurante.getId(),
                "formas-pagamento"));

        restauranteModel.add(this.algaLinks.linkToRestauranteResponsaveis(restaurante.getId(),
                "responsaveis"));

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        return super.toCollectionModel(entities)
                .add(this.algaLinks.linkToRestaurantes());
    }
}
