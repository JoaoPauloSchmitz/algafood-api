package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.model.input.ProdutoInput;
import com.joaopauloschmitz.algafoodapi.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProdutoInputDiassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Produto toDomainObject(ProdutoInput produtoInput) {
        return this.modelMapper.map(produtoInput, Produto.class);
    }

    public void copyToDomainObject(ProdutoInput produtoInput, Produto produto) {
        this.modelMapper.map(produtoInput, produto);
    }
}
