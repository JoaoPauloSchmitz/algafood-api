package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.model.input.UsuarioInput;
import com.joaopauloschmitz.algafoodapi.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioInputDiassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Usuario toDomainObject(UsuarioInput usuarioInput) {
        return this.modelMapper.map(usuarioInput, Usuario.class);
    }

    public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
        this.modelMapper.map(usuarioInput, usuario);
    }
}
