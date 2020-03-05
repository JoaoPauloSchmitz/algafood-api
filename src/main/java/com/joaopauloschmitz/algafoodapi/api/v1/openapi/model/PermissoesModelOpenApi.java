package com.joaopauloschmitz.algafoodapi.api.v1.openapi.model;

import com.joaopauloschmitz.algafoodapi.api.v1.model.PermissaoModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@Data
@ApiModel("PermissoesModel")
public class PermissoesModelOpenApi {

    private PermissoesEmbeddedModelOpenApi _embedded;
    private Links _links;

    @Data
    @ApiModel("PermissoesEmbeddedModel")
    private class PermissoesEmbeddedModelOpenApi {

        private List<PermissaoModel> permissoes;
    }
}
