package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.PedidoInputDisassembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.PedidoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.PedidoResumoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.PedidoModel;
import com.joaopauloschmitz.algafoodapi.api.model.PedidoResumoModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.PedidoInput;
import com.joaopauloschmitz.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.joaopauloschmitz.algafoodapi.domain.exception.NegocioException;
import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import com.joaopauloschmitz.algafoodapi.domain.model.Usuario;
import com.joaopauloschmitz.algafoodapi.domain.repository.PedidoRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.EmissaoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

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

    @GetMapping
    public List<PedidoResumoModel> listar() {
        List<Pedido> todosPedidos = this.pedidoRepository.findAll();
        return this.pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
    }

    @GetMapping("/{id}")
    public PedidoModel buscar(@PathVariable Long id) {
        Pedido pedido = this.emissaoPedidoService.buscarOuFalhar(id);
        return this.pedidoModelAssembler.toModel(pedido);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido pedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            // TODO pegar usuário autenticado
            pedido.setCliente(new Usuario());
            pedido.getCliente().setId(1L);

            pedido = emissaoPedidoService.emitir(pedido);

            return pedidoModelAssembler.toModel(pedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
}
