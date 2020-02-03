package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.FormaPagamentoInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.assembler.FormaPagamentoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.FormaPagamentoModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.FormaPagamentoInput;
import com.joaopauloschmitz.algafoodapi.domain.model.FormaPagamento;
import com.joaopauloschmitz.algafoodapi.domain.repository.FormaPagamentoRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroFormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamentoService;

    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @Autowired
    private FormaPagamentoInputDiassembler formaPagamentoInputDiassembler;

    @GetMapping
    public List<FormaPagamentoModel> listar() {
        return this.formaPagamentoModelAssembler.toCollectionModel(this.formaPagamentoRepository.findAll());
    }

    @GetMapping("/{id}")
    public FormaPagamentoModel buscar(@PathVariable Long id) {
        FormaPagamento formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(id);
        return this.formaPagamentoModelAssembler.toModel(formaPagamento);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamento = this.formaPagamentoInputDiassembler.toDomainObject(formaPagamentoInput);
        return this.formaPagamentoModelAssembler.toModel(this.cadastroFormaPagamentoService.salvar(formaPagamento));
    }

    @PutMapping("/{id}")
    public FormaPagamentoModel atualizar(@PathVariable Long id, @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(id);
        this.formaPagamentoInputDiassembler.copyToDomainObject(formaPagamentoInput, formaPagamento);
        return this.formaPagamentoModelAssembler.toModel(this.cadastroFormaPagamentoService.salvar(formaPagamento));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        this.cadastroFormaPagamentoService.excluir(id);
    }
}
