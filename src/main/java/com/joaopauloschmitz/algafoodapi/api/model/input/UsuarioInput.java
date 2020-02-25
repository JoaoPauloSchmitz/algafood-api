package com.joaopauloschmitz.algafoodapi.api.model.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioInput {

    @ApiModelProperty(example = "João Paulo Schmitz", required = true)
    @NotBlank
    private String nome;

    @ApiModelProperty(example = "joao.paulo@algafood.com.br", required = true)
    @NotBlank
    @Email
    private String email;

}
