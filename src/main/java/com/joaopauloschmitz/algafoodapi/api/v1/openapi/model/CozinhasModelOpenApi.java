package com.joaopauloschmitz.algafoodapi.api.v1.openapi.model;

import com.joaopauloschmitz.algafoodapi.api.v1.model.CidadeModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("CozinhasModel")
@Setter
@Getter
public class CozinhasModelOpenApi {

    private CozinhasEmbeddedModelOpenApi _embedded;
    private Links _links;
    private PageModelOpenApi page;

    @ApiModel("CozinhasEmbeddedModel")
    @Data
    public class CozinhasEmbeddedModelOpenApi {

        private List<CidadeModel> cozinhas;
    }
}
