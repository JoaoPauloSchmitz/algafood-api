package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.PedidoInputDisassembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.PedidoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.PedidoResumoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.PedidoModel;
import com.joaopauloschmitz.algafoodapi.api.model.PedidoResumoModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.PedidoInput;
import com.joaopauloschmitz.algafoodapi.api.openapi.controller.PedidoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.core.data.PageableTranslator;
import com.joaopauloschmitz.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.joaopauloschmitz.algafoodapi.domain.exception.NegocioException;
import com.joaopauloschmitz.algafoodapi.domain.filter.PedidoFilter;
import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import com.joaopauloschmitz.algafoodapi.domain.model.Usuario;
import com.joaopauloschmitz.algafoodapi.domain.repository.PedidoRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.EmissaoPedidoService;
import com.joaopauloschmitz.algafoodapi.infrastructure.repository.spec.PedidoSpecs;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

    @Autowired
    private EmissaoPedidoService emissaoPedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;

    @Autowired
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;

    @Autowired
    private PedidoInputDisassembler pedidoInputDisassembler;

//    @GetMapping
//    public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
//        List<Pedido> pedidos = this.pedidoRepository.findAll();
//        List<PedidoResumoModel> pedidosModel = this.pedidoResumoModelAssembler.toCollectionModel(pedidos);
//
//        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);
//
//        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
//        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());
//
//        if (StringUtils.isNotBlank(campos)) {
//            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
//        }
//
//        pedidosWrapper.setFilters(filterProvider);
//
//        return pedidosWrapper;
//    }

    @GetMapping
    public Page<PedidoResumoModel> pesquisar(PedidoFilter pedidoFilter, @PageableDefault(size = 10) Pageable pageable) {

        pageable = this.traduzirPageable(pageable);
        Page<Pedido> pedidosPage = this.pedidoRepository.findAll(PedidoSpecs.usandoFiltro(pedidoFilter), pageable);

        List<PedidoResumoModel> pedidosResumoModel = this.pedidoResumoModelAssembler.toCollectionModel(pedidosPage.getContent());

        Page<PedidoResumoModel> pedidosResumoModelPage = new PageImpl<>(pedidosResumoModel, pageable,
                pedidosPage.getTotalElements());

        return pedidosResumoModelPage;
    }

    @GetMapping("/{codigoPedido}")
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        Pedido pedido = this.emissaoPedidoService.buscarOuFalhar(codigoPedido);
        return this.pedidoModelAssembler.toModel(pedido);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido pedido = this.pedidoInputDisassembler.toDomainObject(pedidoInput);

            // TODO pegar usuário autenticado
            pedido.setCliente(new Usuario());
            pedido.getCliente().setId(1L);

            pedido = this.emissaoPedidoService.emitir(pedido);

            return this.pedidoModelAssembler.toModel(pedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    public Pageable traduzirPageable(Pageable apiPageable) {
        var mapeamento = Map.of(
                "codigo", "codigo",
                "subtotal", "subtotal",
                "taxaFrete", "taxaFrete",
                "valorTotal", "valorTotal",
                "dataCriacao", "dataCriacao",
                "restaurante.nome", "restaurante.nome",
                "restaurante.id", "restaurante.id",
                "cliente.id", "cliente.id",
                "nomeCliente", "cliente.nome");

        return PageableTranslator.translate(apiPageable, mapeamento);
    }
}
