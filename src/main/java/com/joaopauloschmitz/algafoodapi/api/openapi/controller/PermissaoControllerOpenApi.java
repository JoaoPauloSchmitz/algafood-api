package com.joaopauloschmitz.algafoodapi.api.openapi.controller;

import com.joaopauloschmitz.algafoodapi.api.model.PermissaoModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.CollectionModel;

@Api(tags = "Permissões")
public interface PermissaoControllerOpenApi {

    @ApiOperation("Lista as permissões")
    CollectionModel<PermissaoModel> listar();
}
