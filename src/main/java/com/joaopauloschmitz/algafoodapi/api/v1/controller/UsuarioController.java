package com.joaopauloschmitz.algafoodapi.api.v1.controller;

import com.joaopauloschmitz.algafoodapi.api.v1.assembler.UsuarioInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.UsuarioModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.v1.model.UsuarioModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.SenhaInput;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.UsuarioComSenhaInput;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.UsuarioInput;
import com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller.UsuarioControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.Usuario;
import com.joaopauloschmitz.algafoodapi.domain.repository.UsuarioRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController implements UsuarioControllerOpenApi {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @Autowired
    private UsuarioInputDiassembler usuarioInputDiassembler;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @Override
    @GetMapping
    public CollectionModel<UsuarioModel> listar() {
        List<Usuario> usuarios = this.usuarioRepository.findAll();
        return this.usuarioModelAssembler.toCollectionModel(usuarios);
    }

    @Override
    @GetMapping("/{id}")
    public UsuarioModel buscar(@PathVariable Long id) {
        Usuario usuario = this.cadastroUsuarioService.buscarOuFalhar(id);
        return this.usuarioModelAssembler.toModel(usuario);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioInput) {
        Usuario usuario = this.usuarioInputDiassembler.toDomainObject(usuarioInput);
        return this.usuarioModelAssembler.toModel(this.cadastroUsuarioService.salvar(usuario));
    }

    @Override
    @PutMapping("/{id}")
    public UsuarioModel atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioInput usuarioInput) {
        Usuario usuario = this.cadastroUsuarioService.buscarOuFalhar(id);
        this.usuarioInputDiassembler.copyToDomainObject(usuarioInput, usuario);
        return this.usuarioModelAssembler.toModel(this.cadastroUsuarioService.salvar(usuario));
    }

    @Override
    @PutMapping("/{id}/senha")
    public void alterarSenha(@PathVariable Long id, @RequestBody @Valid SenhaInput senhaInput) {
        this.cadastroUsuarioService.alterarSenha(id, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
    }
}
