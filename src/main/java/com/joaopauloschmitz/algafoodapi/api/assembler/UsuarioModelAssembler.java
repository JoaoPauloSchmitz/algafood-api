package com.joaopauloschmitz.algafoodapi.api.assembler;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.controller.UsuarioController;
import com.joaopauloschmitz.algafoodapi.api.controller.UsuarioGrupoController;
import com.joaopauloschmitz.algafoodapi.api.model.UsuarioModel;
import com.joaopauloschmitz.algafoodapi.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler extends RepresentationModelAssemblerSupport<Usuario, UsuarioModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public UsuarioModelAssembler() {
        super(UsuarioController.class, UsuarioModel.class);
    }

    @Override
    public UsuarioModel toModel(Usuario usuario) {
        UsuarioModel usuarioModel = createModelWithId(usuario.getId(), usuario);
        this.modelMapper.map(usuario, usuarioModel);

        usuarioModel.add(this.algaLinks.linkToUsuarios("usuarios"));

        usuarioModel.add(this.algaLinks.linkToGruposUsuario(usuario.getId(), "grupos-usuario"));

        return usuarioModel;
    }

    @Override
    public CollectionModel<UsuarioModel> toCollectionModel(Iterable<? extends Usuario> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(UsuarioController.class).withSelfRel());
    }
}
