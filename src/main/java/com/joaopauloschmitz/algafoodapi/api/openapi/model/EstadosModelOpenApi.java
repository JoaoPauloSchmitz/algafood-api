package com.joaopauloschmitz.algafoodapi.api.openapi.model;

import com.joaopauloschmitz.algafoodapi.api.model.EstadoModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("EstadosModel")
@Data
public class EstadosModelOpenApi {

    private EstadosEmbeddedModelOpenApi _embedded;
    private Links _links;

    @ApiModel("EstadosEmbeddedModel")
    @Data
    private class EstadosEmbeddedModelOpenApi {

        private List<EstadoModel> estados;

    }
}
