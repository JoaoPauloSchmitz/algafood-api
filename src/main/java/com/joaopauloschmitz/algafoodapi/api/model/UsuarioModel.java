package com.joaopauloschmitz.algafoodapi.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioModel {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "João Paulo Schmitz")
    private String nome;

    @ApiModelProperty(example = "joao.paulo@algafood.com.br")
    private String email;

}
