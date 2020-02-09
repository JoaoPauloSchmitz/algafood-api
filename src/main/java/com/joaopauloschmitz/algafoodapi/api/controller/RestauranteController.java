package com.joaopauloschmitz.algafoodapi.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.joaopauloschmitz.algafoodapi.api.assembler.RestauranteInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.RestauranteModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.RestauranteModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.RestauranteInput;
import com.joaopauloschmitz.algafoodapi.api.model.view.RestauranteView;
import com.joaopauloschmitz.algafoodapi.domain.exception.CidadeNaoEncontradaException;
import com.joaopauloschmitz.algafoodapi.domain.exception.CozinhaNaoEncontradaException;
import com.joaopauloschmitz.algafoodapi.domain.exception.NegocioException;
import com.joaopauloschmitz.algafoodapi.domain.exception.RestauranteNaoEncontradoException;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import com.joaopauloschmitz.algafoodapi.domain.repository.RestauranteRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private RestauranteModelAssembler restauranteModelAssembler;

    @Autowired
    private RestauranteInputDiassembler restauranteInputDiassembler;

    @JsonView(RestauranteView.Resumo.class)
    @GetMapping
    public List<RestauranteModel> listar() {
        return this.restauranteModelAssembler.toCollectionModel(this.restauranteRepository.findAll());
    }

    @JsonView(RestauranteView.ApenasNome.class)
    @GetMapping(params = "projecao=apenas-nome")
    public List<RestauranteModel> listarApenasNomes() {
        return listar();
    }

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

    @PutMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable Long id) {
        try {
            this.cadastroRestauranteService.ativar(id);

        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable Long id) {
        try {
            this.cadastroRestauranteService.inativar(id);

        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarMultiplos(@RequestBody List<Long> restautantesId) {
        this.cadastroRestauranteService.ativar(restautantesId);
    }

    @DeleteMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarMultiplos(@RequestBody List<Long> restautantesId) {
        this.cadastroRestauranteService.inativar(restautantesId);
    }

    @PutMapping("/{id}/abertura")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void abrir(@PathVariable Long id) {
        this.cadastroRestauranteService.abrir(id);
    }

    @PutMapping("/{id}/fechamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fechar(@PathVariable Long id) {
        this.cadastroRestauranteService.fechar(id);
    }
}
