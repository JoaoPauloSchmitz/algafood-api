package com.joaopauloschmitz.algafoodapi.api.v1.controller;

import com.joaopauloschmitz.algafoodapi.api.v1.assembler.FormaPagamentoInputDiassembler;
import com.joaopauloschmitz.algafoodapi.api.v1.assembler.FormaPagamentoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.v1.model.FormaPagamentoModel;
import com.joaopauloschmitz.algafoodapi.api.v1.model.input.FormaPagamentoInput;
import com.joaopauloschmitz.algafoodapi.api.v1.openapi.controller.FormaPagamentoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.model.FormaPagamento;
import com.joaopauloschmitz.algafoodapi.domain.repository.FormaPagamentoRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroFormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/v1/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamentoService;

    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @Autowired
    private FormaPagamentoInputDiassembler formaPagamentoInputDiassembler;

    @Override
    @GetMapping
    public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";

        OffsetDateTime dataUltimaAtualizacao = this.formaPagamentoRepository.getDataUltimaAtualizacao();

        if (dataUltimaAtualizacao != null) {
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        List<FormaPagamento> formaPagamentos = this.formaPagamentoRepository.findAll();

        CollectionModel<FormaPagamentoModel> formaPagamentoModels = this.formaPagamentoModelAssembler
                .toCollectionModel(formaPagamentos);

        return ResponseEntity.ok()
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
//				.cacheControl(CacheControl.noCache())
//              .cacheControl(CacheControl.noStore())
               .body(formaPagamentoModels);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long id, ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";

        OffsetDateTime dataAtualizacao = formaPagamentoRepository
                .getDataAtualizacaoById(id);

        if (dataAtualizacao != null) {
            eTag = String.valueOf(dataAtualizacao.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }
        FormaPagamento formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(id);
        FormaPagamentoModel formaPagamentoModel = this.formaPagamentoModelAssembler.toModel(formaPagamento);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                .body(formaPagamentoModel);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamento = this.formaPagamentoInputDiassembler.toDomainObject(formaPagamentoInput);
        return this.formaPagamentoModelAssembler.toModel(this.cadastroFormaPagamentoService.salvar(formaPagamento));
    }

    @Override
    @PutMapping("/{id}")
    public FormaPagamentoModel atualizar(@PathVariable Long id, @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamento = this.cadastroFormaPagamentoService.buscarOuFalhar(id);
        this.formaPagamentoInputDiassembler.copyToDomainObject(formaPagamentoInput, formaPagamento);
        return this.formaPagamentoModelAssembler.toModel(this.cadastroFormaPagamentoService.salvar(formaPagamento));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        this.cadastroFormaPagamentoService.excluir(id);
    }
}
