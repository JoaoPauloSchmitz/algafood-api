package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.v1.controller.CozinhaController;
import com.joaopauloschmitz.algafoodapi.api.v1.model.CozinhaModel;
import com.joaopauloschmitz.algafoodapi.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CozinhaModelAssembler extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public CozinhaModelAssembler() {
        super(CozinhaController.class, CozinhaModel.class);
    }

    @Override
    public CozinhaModel toModel(Cozinha cozinha) {
        CozinhaModel cozinhaModel = createModelWithId(cozinha.getId(), cozinha);
        this.modelMapper.map(cozinha, cozinhaModel);

        cozinhaModel.add(this.algaLinks.linkToCozinhas("cozinhas"));

        return cozinhaModel;
    }

}
