package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.v1.controller.GrupoController;
import com.joaopauloschmitz.algafoodapi.api.v1.model.GrupoModel;
import com.joaopauloschmitz.algafoodapi.domain.model.Grupo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class GrupoModelAssembler extends RepresentationModelAssemblerSupport<Grupo, GrupoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public GrupoModelAssembler() {
        super(GrupoController.class, GrupoModel.class);
    }

    @Override
    public GrupoModel toModel(Grupo grupo) {
        GrupoModel grupoModel = createModelWithId(grupo.getId(), grupo);
        this.modelMapper.map(grupo, grupoModel);

        grupoModel.add(this.algaLinks.linkToGrupos("grupos"));

        grupoModel.add(this.algaLinks.linkToGrupoPermissoes(grupo.getId(), "permissoes"));

        return grupoModel;
    }

    @Override
    public CollectionModel<GrupoModel> toCollectionModel(Iterable<? extends Grupo> entities) {
        return super.toCollectionModel(entities)
                .add(this.algaLinks.linkToGrupos());
    }
}
