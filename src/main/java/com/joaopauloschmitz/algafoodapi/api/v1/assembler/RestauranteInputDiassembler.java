package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.model.input.RestauranteInput;
import com.joaopauloschmitz.algafoodapi.domain.model.Cidade;
import com.joaopauloschmitz.algafoodapi.domain.model.Cozinha;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestauranteInputDiassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Restaurante toDomainObject(RestauranteInput restauranteInput) {
        return this.modelMapper.map(restauranteInput, Restaurante.class);
    }

    public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
        // Para evitar org.hibernate.HibernateException: identifier of an instance of
        // com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
        restaurante.setCozinha(new Cozinha());

        if (restaurante.getEndereco() != null) {
            restaurante.getEndereco().setCidade(new Cidade());
        }

        this.modelMapper.map(restauranteInput, restaurante);
    }
}
