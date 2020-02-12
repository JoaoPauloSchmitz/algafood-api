package com.joaopauloschmitz.algafoodapi.domain.service;

import com.joaopauloschmitz.algafoodapi.domain.exception.FotoProdutoNaoEncontradaException;
import com.joaopauloschmitz.algafoodapi.domain.model.FotoProduto;
import com.joaopauloschmitz.algafoodapi.domain.repository.ProdutoRepository;
import com.joaopauloschmitz.algafoodapi.domain.service.FotoStorageService.NovaFoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Optional;

@Service
public class CatalogoFotoProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FotoStorageService fotoStorageService;

    @Transactional
    public FotoProduto salvar(FotoProduto fotoProduto, InputStream dadosArquivo) {
        Long restauranteId = fotoProduto.getRestauranteId();
        Long produtoId = fotoProduto.getProduto().getId();
        String nomeNovoArquivo = this.fotoStorageService.gerarNomeArquivo(fotoProduto.getNomeArquivo());
        String nomeArquivoExistente = null;

        Optional<FotoProduto> fotoExistente = this.produtoRepository.findFotoById(restauranteId, produtoId);

        if (fotoExistente.isPresent()) {
            nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
            this.produtoRepository.delete(fotoExistente.get());
        }

        fotoProduto.setNomeArquivo(nomeNovoArquivo);
        fotoProduto = this.produtoRepository.save(fotoProduto);
        this.produtoRepository.flush();

        NovaFoto novaFoto = NovaFoto.builder()
                .nomeArquivo(fotoProduto.getNomeArquivo())
                .inputStream(dadosArquivo)
                .contentType(fotoProduto.getContentType())
                .build();

        this.fotoStorageService.substituir(nomeArquivoExistente, novaFoto);

        return fotoProduto;
    }

    @Transactional
    public void excluir(Long restauranteId, Long produtoId) {
        FotoProduto fotoProduto = this.buscarOuFalhar(restauranteId, produtoId);

        this.produtoRepository.delete(fotoProduto);
        this.produtoRepository.flush();

        this.fotoStorageService.remover(fotoProduto.getNomeArquivo());
    }

    public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
        return this.produtoRepository.findFotoById(restauranteId, produtoId)
                .orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
    }
}
