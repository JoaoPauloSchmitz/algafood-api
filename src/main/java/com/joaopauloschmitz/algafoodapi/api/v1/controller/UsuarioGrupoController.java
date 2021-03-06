package com.joaopauloschmitz.algafoodapi.api.v1.controller;

import com.joaopauloschmitz.algafoodapi.api.v1.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.GrupoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.v1.model.GrupoModel;
import com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller.UsuarioGrupoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.core.security.CheckSecurity;
import com.joaopauloschmitz.algafoodapi.domain.model.Usuario;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/usuarios/{usuarioId}/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioGrupoController implements UsuarioGrupoControllerOpenApi {

    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private AlgaLinks algaLinks;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @Override
    @GetMapping
    public CollectionModel<GrupoModel> listar(@PathVariable Long usuarioId) {
        Usuario usuario = this.cadastroUsuarioService.buscarOuFalhar(usuarioId);

        CollectionModel<GrupoModel> grupoModels = this.grupoModelAssembler
                .toCollectionModel(usuario.getGrupos())
                .removeLinks()
                .add(this.algaLinks.linkToUsuarioGrupoAssociacao(usuarioId, "associar"));

        grupoModels.getContent().forEach(grupoModel -> {
            grupoModel.add(this.algaLinks.linkToUsuarioGrupoDesassociacao(usuarioId, grupoModel.getId(), "desassociar"));
        });

        return grupoModels;
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @Override
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
        this.cadastroUsuarioService.desassociarGrupo(usuarioId, grupoId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @Override
    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
        this.cadastroUsuarioService.associarGrupo(usuarioId, grupoId);
        return ResponseEntity.noContent().build();
    }
}
