package com.joaopauloschmitz.algafoodapi.api.model.input;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class EstadoInput {

    @ApiModelProperty(example = "1", required = true)
    @NotBlank
    private String nome;
}
