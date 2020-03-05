package com.joaopauloschmitz.algafoodapi.api.v1.openapi.model;

import com.joaopauloschmitz.algafoodapi.api.v1.model.RestauranteBasicoModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("RestaurantesBasicoModel")
@Setter
@Getter
public class RestaurantesBasicoModelOpenApi {

    private RestaurantesEmbeddedModelOpenApi _embedded;
    private Links _links;

    @ApiModel("RestaurantesEmbeddedModel")
    @Data
    public class RestaurantesEmbeddedModelOpenApi {

        private List<RestauranteBasicoModel> restaurantes;

    }
}
