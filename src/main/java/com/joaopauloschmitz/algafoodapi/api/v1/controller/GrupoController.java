package com.joaopauloschmitz.algafoodapi.api.v1.controller;

import com.joaopauloschmitz.algafoodapi.api.v1.assembler.GrupoInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.GrupoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.v1.model.GrupoModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.GrupoInput;
import com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller.GrupoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.Grupo;
import com.joaopauloschmitz.algafoodapi.domain.repository.GrupoRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CadastroGrupoService cadastroGrupoService;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private GrupoInputDiassembler grupoInputDiassembler;

    @GetMapping
    public CollectionModel<GrupoModel> listar() {
        List<Grupo> grupos = this.grupoRepository.findAll();
        return this.grupoModelAssembler.toCollectionModel(grupos);
    }

    @Override
    @GetMapping("/{id}")
    public GrupoModel buscar(@PathVariable Long id) {
        Grupo grupo = this.cadastroGrupoService.buscarOuFalhar(id);
        return this.grupoModelAssembler.toModel(grupo);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupo = this.grupoInputDiassembler.toDomainObject(grupoInput);
        return this.grupoModelAssembler.toModel(this.cadastroGrupoService.salvar(grupo));
    }

    @Override
    @PutMapping("/{id}")
    public GrupoModel atualizar(@PathVariable Long id,
                                       @RequestBody @Valid GrupoInput grupoInput) {

        Grupo grupo = this.cadastroGrupoService.buscarOuFalhar(id);
        this.grupoInputDiassembler.copyToDomainObject(grupoInput, grupo);

        return this.grupoModelAssembler.toModel(this.cadastroGrupoService.salvar(grupo));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        this.cadastroGrupoService.excluir(id);
    }
}
