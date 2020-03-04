package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.assembler.FotoProdutoModelAssembler;
import com.joaopauloschmitz.algafoodapi.api.model.FotoProdutoModel;
import com.joaopauloschmitz.algafoodapi.api.model.input.FotoProdutoInput;
import com.joaopauloschmitz.algafoodapi.api.openapi.controller.RestauranteProdutoFotoControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.joaopauloschmitz.algafoodapi.domain.model.FotoProduto;
import com.joaopauloschmitz.algafoodapi.domain.model.Produto;
import com.joaopauloschmitz.algafoodapi.domain.service.CadastroProdutoService;
import com.joaopauloschmitz.algafoodapi.domain.service.CatalogoFotoProdutoService;
import com.joaopauloschmitz.algafoodapi.domain.service.FotoStorageService;
import com.joaopauloschmitz.algafoodapi.domain.service.FotoStorageService.FotoRecuperada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/produtos/{produtoId}/foto",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoFotoController implements RestauranteProdutoFotoControllerOpenApi {

    @Autowired
    private CatalogoFotoProdutoService catalogoFotoProdutoService;

    @Autowired
    private CadastroProdutoService cadastroProdutoService;

    @Autowired
    private FotoStorageService fotoStorageService;

    @Autowired
    private FotoProdutoModelAssembler fotoProdutoModelAssembler;

    @Override
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId,
                       @PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput,
                       @RequestPart(required = true) MultipartFile multipartFile) throws IOException {

        Produto produto = this.cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);

//        MultipartFile multipartFile = fotoProdutoInput.getArquivo();

        FotoProduto fotoProduto = new FotoProduto();
        fotoProduto.setProduto(produto);
        fotoProduto.setDescricao(fotoProdutoInput.getDescricao());
        fotoProduto.setContentType(multipartFile.getContentType());
        fotoProduto.setTamanho(multipartFile.getSize());
        fotoProduto.setNomeArquivo(multipartFile.getOriginalFilename());

        FotoProduto fotoSalva = this.catalogoFotoProdutoService.salvar(fotoProduto, multipartFile.getInputStream());

        return this.fotoProdutoModelAssembler.toModel(fotoSalva);
    }

    @Override
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        this.catalogoFotoProdutoService.excluir(restauranteId, produtoId);
    }

    @Override
    @GetMapping
    public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        FotoProduto fotoProduto = this.catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
        return this.fotoProdutoModelAssembler.toModel(fotoProduto);
    }

    @Override
    @GetMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> servirFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
            @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {

        try {
            FotoProduto fotoProduto = this.catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);

            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
            List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);

            verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);

            FotoRecuperada fotoRecuperada = this.fotoStorageService.recuperar(fotoProduto.getNomeArquivo());

            if (fotoRecuperada.temUrl()) {
                return ResponseEntity
                        .status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
                        .build();
            } else {
                return ResponseEntity.ok()
                        .contentType(mediaTypeFoto)
                        .body(new InputStreamResource(fotoRecuperada.getInputStream()));
            }

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto,
                          List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {

        boolean compativel = mediaTypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
        }
    }
}
