package com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller;

import com.joaopauloschmitz.algafoodapi.api.exceptionhandler.Problem;
import com.joaopauloschmitz.algafoodapi.api.v1.model.CidadeModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.CidadeInput;
import io.swagger.annotations.*;
import org.springframework.hateoas.CollectionModel;

@Api(tags = "Cidades")
public interface CidadeControllerOpenApi {

    @ApiOperation("Lista as cidades")
    public CollectionModel<CidadeModel> listar();

    @ApiOperation("Busca uma cidade por ID")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID da cidade inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    public CidadeModel buscar(@ApiParam(name = "cidadeID", value = "ID de uma cidade", example = "1", required = true)
                              Long id);

    @ApiOperation("Cadastra uma cidade por ID")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cidade cadastrada")
    })
    public CidadeModel adicionar(@ApiParam(name = "corpo", value = "Representação de uma nova cidade", required = true)
                                 CidadeInput cidadeInput);

    @ApiOperation("Atualiza uma cidade por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cidade atualizada"),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    public CidadeModel atualizar(@ApiParam(value = "ID de uma cidade", example = "1", required = true)
                                 Long id,

                                 @ApiParam(name = "corpo", value = "Representação de uma nova cidade com os novos dados")
                                 CidadeInput cidadeInput);

    @ApiOperation("Exclui uma cidade por ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cidade excluida"),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    public void remover(@ApiParam(value = "ID de uma cidade", example = "1", required = true)
                        Long id);
}
