package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.GrupoInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.GrupoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.openapi.controller.GrupoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.api.model.GrupoModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.GrupoInput;
import com.joaopauloschmitz.algafoodapi.domain.model.Grupo;
import com.joaopauloschmitz.algafoodapi.domain.repository.GrupoRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public List<GrupoModel> listar() {
        return this.grupoModelAssembler.toCollectionModel(this.grupoRepository.findAll());
    }

    @GetMapping("/{id}")
    public GrupoModel buscar(@PathVariable Long id) {
        Grupo grupo = this.cadastroGrupoService.buscarOuFalhar(id);
        return this.grupoModelAssembler.toModel(grupo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupo = this.grupoInputDiassembler.toDomainObject(grupoInput);
        return this.grupoModelAssembler.toModel(this.cadastroGrupoService.salvar(grupo));
    }

    @PutMapping("/{id}")
    public GrupoModel atualizar(@PathVariable Long id,
                                       @RequestBody @Valid GrupoInput grupoInput) {

        Grupo grupo = this.cadastroGrupoService.buscarOuFalhar(id);
        this.grupoInputDiassembler.copyToDomainObject(grupoInput, grupo);

        return this.grupoModelAssembler.toModel(this.cadastroGrupoService.salvar(grupo));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        this.cadastroGrupoService.excluir(id);
    }
}
