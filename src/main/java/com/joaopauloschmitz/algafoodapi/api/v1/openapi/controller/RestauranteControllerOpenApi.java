package com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller;

import com.joaopauloschmitz.algafoodapi.api.exceptionhandler.Problem;
import com.joaopauloschmitz.algafoodapi.api.v1.model.RestauranteApenasNomeModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.RestauranteBasicoModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.RestauranteModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.RestauranteInput;
import com.joaopauloschmitz.algafoodapi.api.v1.openapi.model.RestaurantesBasicoModelOpenApi;
import io.swagger.annotations.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "Restaurantes")
public interface RestauranteControllerOpenApi {

    @ApiOperation(value = "Lista restaurantes", response = RestaurantesBasicoModelOpenApi.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "Nome da projeção de pedidos", allowableValues = "apenas-nome",
                    name = "projecao", paramType = "query", type = "string")
    })
//    @JsonView(RestauranteView.Resumo.class)
    public CollectionModel<RestauranteBasicoModel> listar();

    @ApiIgnore
    @ApiOperation(value = "Lista restaurantes", hidden = true)
    public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes();

    @ApiOperation("Busca um restaurante por ID")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID do restaurante inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public RestauranteModel buscar(
            @ApiParam(value = "ID de um restaurante", example = "1", required = true)
                    Long restauranteId);

    @ApiOperation("Cadastra um restaurante")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Restaurante cadastrado"),
    })
    public RestauranteModel adicionar(
            @ApiParam(name = "corpo", value = "Representação de um novo restaurante", required = true)
                    RestauranteInput restauranteInput);

    @ApiOperation("Atualiza um restaurante por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Restaurante atualizado"),
            @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public RestauranteModel atualizar(
            @ApiParam(value = "ID de um restaurante", example = "1", required = true)
                    Long restauranteId,

            @ApiParam(name = "corpo", value = "Representação de um restaurante com os novos dados",
                    required = true)
                    RestauranteInput restauranteInput);

    @ApiOperation("Ativa um restaurante por ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Restaurante ativado com sucesso"),
            @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public ResponseEntity<Void> ativar(
            @ApiParam(value = "ID de um restaurante", example = "1", required = true)
                    Long restauranteId);

    @ApiOperation("Inativa um restaurante por ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Restaurante inativado com sucesso"),
            @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public ResponseEntity<Void> inativar(
            @ApiParam(value = "ID de um restaurante", example = "1", required = true)
                    Long restauranteId);

    @ApiOperation("Ativa múltiplos restaurantes")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Restaurantes ativados com sucesso")
    })
    public void ativarMultiplos(
            @ApiParam(name = "corpo", value = "IDs de restaurantes", required = true)
                    List<Long> restauranteIds);

    @ApiOperation("Inativa múltiplos restaurantes")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Restaurantes ativados com sucesso")
    })
    public void inativarMultiplos(
            @ApiParam(name = "corpo", value = "IDs de restaurantes", required = true)
                    List<Long> restauranteIds);

    @ApiOperation("Abre um restaurante por ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Restaurante aberto com sucesso"),
            @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public ResponseEntity<Void> abrir(
            @ApiParam(value = "ID de um restaurante", example = "1", required = true)
                    Long restauranteId);

    @ApiOperation("Fecha um restaurante por ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Restaurante fechado com sucesso"),
            @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public ResponseEntity<Void> fechar(
            @ApiParam(value = "ID de um restaurante", example = "1", required = true)
                    Long restauranteId);
}