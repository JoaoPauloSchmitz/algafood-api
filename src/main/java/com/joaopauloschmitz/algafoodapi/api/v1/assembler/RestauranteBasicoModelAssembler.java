package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.v1.controller.RestauranteController;
import com.joaopauloschmitz.algafoodapi.api.v1.model.RestauranteBasicoModel;
import com.joaopauloschmitz.algafoodapi.core.security.AlgaSecurity;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestauranteBasicoModelAssembler
        extends RepresentationModelAssemblerSupport<Restaurante, RestauranteBasicoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public RestauranteBasicoModelAssembler() {
        super(RestauranteController.class, RestauranteBasicoModel.class);
    }

    @Override
    public RestauranteBasicoModel toModel(Restaurante restaurante) {
        RestauranteBasicoModel restauranteModel = createModelWithId(
                restaurante.getId(), restaurante);

        this.modelMapper.map(restaurante, restauranteModel);

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
        }

        if (this.algaSecurity.podeConsultarCozinhas()) {
            restauranteModel.getCozinha().add(
                    this.algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
        }

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteBasicoModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        CollectionModel<RestauranteBasicoModel> collectionModel = super.toCollectionModel(entities);

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            collectionModel.add(this.algaLinks.linkToRestaurantes());
        }

        return collectionModel;
    }
}
