package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.v1.controller.RestauranteController;
import com.joaopauloschmitz.algafoodapi.api.v1.model.RestauranteApenasNomeModel;
import com.joaopauloschmitz.algafoodapi.core.security.AlgaSecurity;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestauranteApenasNomeModelAssembler
        extends RepresentationModelAssemblerSupport<Restaurante, RestauranteApenasNomeModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public RestauranteApenasNomeModelAssembler() {
        super(RestauranteController.class, RestauranteApenasNomeModel.class);
    }

    @Override
    public RestauranteApenasNomeModel toModel(Restaurante restaurante) {
        RestauranteApenasNomeModel restauranteModel = createModelWithId(
                restaurante.getId(), restaurante);

        this.modelMapper.map(restaurante, restauranteModel);

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
        }

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteApenasNomeModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        CollectionModel<RestauranteApenasNomeModel> collectionModel = super.toCollectionModel(entities);

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            collectionModel.add(this.algaLinks.linkToRestaurantes());
        }

        return collectionModel;
    }
}
