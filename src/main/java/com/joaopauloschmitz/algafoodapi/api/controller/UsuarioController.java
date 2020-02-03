package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.UsuarioInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.UsuarioModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.UsuarioModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.SenhaInput;
import com.joaopauloschmitz.algafoodapi.api.model.input.UsuarioComSenhaInput;
import com.joaopauloschmitz.algafoodapi.api.model.input.UsuarioInput;
import com.joaopauloschmitz.algafoodapi.domain.model.Usuario;
import com.joaopauloschmitz.algafoodapi.domain.repository.UsuarioRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @Autowired
    private UsuarioInputDiassembler usuarioInputDiassembler;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @GetMapping
    public List<UsuarioModel> listar() {
        return this.usuarioModelAssembler.toCollectionModel(this.usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public UsuarioModel buscar(@PathVariable Long id) {
        Usuario usuario = this.cadastroUsuarioService.buscarOuFalhar(id);
        return this.usuarioModelAssembler.toModel(usuario);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioInput) {
        Usuario usuario = this.usuarioInputDiassembler.toDomainObject(usuarioInput);
        return this.usuarioModelAssembler.toModel(this.cadastroUsuarioService.salvar(usuario));
    }

    @PutMapping("/{id}")
    public UsuarioModel atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioInput usuarioInput) {
        Usuario usuario = this.cadastroUsuarioService.buscarOuFalhar(id);
        this.usuarioInputDiassembler.copyToDomainObject(usuarioInput, usuario);
        return this.usuarioModelAssembler.toModel(this.cadastroUsuarioService.salvar(usuario));
    }

    @PutMapping("/{id}/senha")
    public void alterarSenha(@PathVariable Long id, @RequestBody @Valid SenhaInput senhaInput) {
        this.cadastroUsuarioService.alterarSenha(id, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
    }
}
