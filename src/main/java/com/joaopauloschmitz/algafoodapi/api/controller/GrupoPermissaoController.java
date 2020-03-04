package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.assembler.PermissaoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.PermissaoModel;
import com.joaopauloschmitz.algafoodapi.api.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.Grupo;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/grupos/{grupoId}/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {

    @Autowired
    private CadastroGrupoService cadastroGrupoService;

    @Autowired
    private PermissaoModelAssembler permissaoModelAssembler;

    @Autowired
    private AlgaLinks algaLinks;

    @Override
    @GetMapping
    public CollectionModel<PermissaoModel> listar(@PathVariable Long grupoId) {
        Grupo grupo = this.cadastroGrupoService.buscarOuFalhar(grupoId);

        CollectionModel<PermissaoModel> permissaoModels = this.permissaoModelAssembler
                .toCollectionModel(grupo.getPermissoes())
                .removeLinks()
                .add(this.algaLinks.linkToGrupoPermissoes(grupoId))
                .add(this.algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));

        permissaoModels.getContent().forEach(permissaoModel -> {
            permissaoModel.add(this.algaLinks.linkToGrupoPermissaoDesassociacao(
                    grupoId, permissaoModel.getId(), "desassociar"));
        });

        return permissaoModels;
    }

    @Override
    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        this.cadastroGrupoService.desassociarPermissao(grupoId, permissaoId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        this.cadastroGrupoService.associarPermissao(grupoId, permissaoId);
        return ResponseEntity.noContent().build();
    }
}
