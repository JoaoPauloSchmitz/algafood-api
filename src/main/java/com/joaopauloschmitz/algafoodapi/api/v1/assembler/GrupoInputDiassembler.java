package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.model.input.GrupoInput;
import com.joaopauloschmitz.algafoodapi.domain.model.Grupo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrupoInputDiassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Grupo toDomainObject(GrupoInput grupoInput) {
        return this.modelMapper.map(grupoInput, Grupo.class);
    }

    public void copyToDomainObject(GrupoInput grupoInput, Grupo grupo) {
        this.modelMapper.map(grupoInput, grupo);
    }
}
