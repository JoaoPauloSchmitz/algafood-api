package com.joaopauloschmitz.algafoodapi.api.v1.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.RestauranteApenasNomeModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.RestauranteInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.RestauranteModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.v1.model.RestauranteApenasNomeModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.RestauranteBasicoModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.RestauranteModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.RestauranteInput;
import com.joaopauloschmitz.algafoodapi.api.v1.model.view.RestauranteView;
import com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller.RestauranteControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.core.security.CheckSecurity;
import com.joaopauloschmitz.algafoodapi.domain.exception.CidadeNaoEncontradaException;
import com.joaopauloschmitz.algafoodapi.domain.exception.CozinhaNaoEncontradaException;
import com.joaopauloschmitz.algafoodapi.domain.exception.NegocioException;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import com.joaopauloschmitz.algafoodapi.domain.repository.RestauranteRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements RestauranteControllerOpenApi {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private RestauranteModelAssembler restauranteModelAssembler;

    @Autowired
    private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;

    @Autowired
    private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler;

    @Autowired
    private RestauranteInputDiassembler restauranteInputDiassembler;

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
//    @JsonView(RestauranteView.Resumo.class)
    @GetMapping
    public CollectionModel<RestauranteBasicoModel> listar() {
        return this.restauranteBasicoModelAssembler
                .toCollectionModel(this.restauranteRepository.findAll());
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @JsonView(RestauranteView.ApenasNome.class)
    @GetMapping(params = "projecao=apenas-nome")
    public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
        return this.restauranteApenasNomeModelAssembler
                .toCollectionModel(this.restauranteRepository.findAll());
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping("/{id}")
    public RestauranteModel buscar(@PathVariable Long id) {
        Restaurante restaurante = this.cadastroRestauranteService.buscarOuFalhar(id);
        return this.restauranteModelAssembler.toModel(restaurante);
    }

//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
//		List<Restaurante> restaurantes = this.cadastroRestauranteService.findAll();
//		List<RestauranteModel> restaurantesModel = this.restauranteModelAssembler.toCollectionModel(restaurantes);
//
//		MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restaurantesModel);
//
//		restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);
//
//		if ("apenas-nome".equals(projecao)) {
//			restaurantesWrapper.setSerializationView(RestauranteView.ApenasNome.class);
//		} else if ("completo".equals(projecao)) {
//			restaurantesWrapper.setSerializationView(null);
//		}
//
//		return restaurantesWrapper;
//	}

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
        try {
            Restaurante restaurante = this.restauranteInputDiassembler.toDomainObject(restauranteInput);
            return this.restauranteModelAssembler.toModel(this.cadastroRestauranteService.salvar(restaurante));
        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PutMapping
    public RestauranteModel atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteInput restauranteInput) {
        try {
            Restaurante restauranteAtual = this.cadastroRestauranteService.buscarOuFalhar(id);
            this.restauranteInputDiassembler.copyToDomainObject(restauranteInput, restauranteAtual);

            return this.restauranteModelAssembler.toModel(this.cadastroRestauranteService.salvar(restauranteAtual));
        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PutMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        this.cadastroRestauranteService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @DeleteMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        this.cadastroRestauranteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarMultiplos(@RequestBody List<Long> restautantesId) {
        this.cadastroRestauranteService.ativar(restautantesId);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @DeleteMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarMultiplos(@RequestBody List<Long> restautantesId) {
        this.cadastroRestauranteService.inativar(restautantesId);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @PutMapping("/{id}/abertura")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> abrir(@PathVariable Long id) {
        this.cadastroRestauranteService.abrir(id);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @PutMapping("/{id}/fechamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> fechar(@PathVariable Long id) {
        this.cadastroRestauranteService.fechar(id);
        return ResponseEntity.noContent().build();
    }
}
