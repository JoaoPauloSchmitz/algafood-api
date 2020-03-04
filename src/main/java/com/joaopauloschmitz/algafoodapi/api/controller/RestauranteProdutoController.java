package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.assembler.ProdutoInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.ProdutoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.ProdutoModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.ProdutoInput;
import com.joaopauloschmitz.algafoodapi.api.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.Produto;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import com.joaopauloschmitz.algafoodapi.domain.repository.ProdutoRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroProdutoService;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CadastroProdutoService cadastroProdutoService;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private ProdutoModelAssembler produtoModelAssembler;

    @Autowired
    private ProdutoInputDiassembler produtoInputDiassembler;

    @Autowired
    private AlgaLinks algaLinks;

    @Override
    @GetMapping
    public CollectionModel<ProdutoModel> listar(@PathVariable Long restauranteId,
                    @RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {

        Restaurante restaurante = this.cadastroRestauranteService.buscarOuFalhar(restauranteId);
        List<Produto> todosProdutos;

        if (incluirInativos) {
            todosProdutos = this.produtoRepository.findTodosByRestaurante(restaurante);
        } else {
            todosProdutos = this.produtoRepository.findAtivosByRestaurante(restaurante);
        }

        return this.produtoModelAssembler.toCollectionModel(todosProdutos)
                .add(this.algaLinks.linkToProdutos(restauranteId));
    }

    @Override
    @GetMapping("/{produtoId}")
    public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        Produto produto = this.cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
        return this.produtoModelAssembler.toModel(produto);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoModel adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInput produtoInput) {
        Restaurante restaurante = this.cadastroRestauranteService.buscarOuFalhar(restauranteId);

        Produto produto = this.produtoInputDiassembler.toDomainObject(produtoInput);
        produto.setRestaurante(restaurante);

        return this.produtoModelAssembler.toModel(this.cadastroProdutoService.salvar(produto));
    }

    @Override
    @PutMapping("/{produtoId}")
    public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                                  @RequestBody @Valid ProdutoInput produtoInput) {

        Produto produto = this.cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
        this.produtoInputDiassembler.copyToDomainObject(produtoInput, produto);

        return this.produtoModelAssembler.toModel(this.cadastroProdutoService.salvar(produto));
    }
}
