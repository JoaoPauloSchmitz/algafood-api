package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.model.input.FormaPagamentoInput;
import com.joaopauloschmitz.algafoodapi.domain.model.FormaPagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormaPagamentoInputDiassembler {

    @Autowired
    private ModelMapper modelMapper;

    public FormaPagamento toDomainObject(FormaPagamentoInput formaPagamentoInput) {
        return this.modelMapper.map(formaPagamentoInput, FormaPagamento.class);
    }

    public void copyToDomainObject(FormaPagamentoInput formaPagamentoInput, FormaPagamento formaPagamento) {
        this.modelMapper.map(formaPagamentoInput, formaPagamento);
    }
}
