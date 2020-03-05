package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.model.input.CozinhaInput;
import com.joaopauloschmitz.algafoodapi.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CozinhaInputDiassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Cozinha toDomainObject(CozinhaInput cozinhaInput) {
        return this.modelMapper.map(cozinhaInput, Cozinha.class);
    }

    public void copyToDomainObject(CozinhaInput cozinhaInput, Cozinha cozinha) {
        this.modelMapper.map(cozinhaInput, cozinha);
    }
}
