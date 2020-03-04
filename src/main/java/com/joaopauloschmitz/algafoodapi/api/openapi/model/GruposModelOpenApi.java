package com.joaopauloschmitz.algafoodapi.api.openapi.model;

import com.joaopauloschmitz.algafoodapi.api.model.GrupoModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@Data
@ApiModel("GruposModel")
public class GruposModelOpenApi {

    private GruposEmbeddedModelOpenApi _embedded;
    private Links _links;

    @ApiModel("GruposEmbeddedModel")
    @Data
    private class GruposEmbeddedModelOpenApi {

        private List<GrupoModel> grupos;
    }
}
