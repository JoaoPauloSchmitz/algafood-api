package com.joaopauloschmitz.algafoodapi.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

//@ApiModel(description = "Representa uma cidade")
@Getter
@Setter
public class CidadeModel {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "Jaraguá do Sul")
    private String nome;

    private EstadoModel estado;
}
