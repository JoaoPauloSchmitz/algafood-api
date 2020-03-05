package com.joaopauloschmitz.algafoodapi.api.v1.openapi.model;

import com.joaopauloschmitz.algafoodapi.api.v1.model.FormaPagamentoModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("FormasPagamentoModel")
@Data
public class FormasPagamentoModelOpenApi {

    private FormasPagamentoEmbeddedModelOpenApi _embedded;
    private Links _links;

    @ApiModel("FormasPagamentoEmbeddedModel")
    @Data
    private class FormasPagamentoEmbeddedModelOpenApi {

        private List<FormaPagamentoModel> formasPagamento;

    }
}
