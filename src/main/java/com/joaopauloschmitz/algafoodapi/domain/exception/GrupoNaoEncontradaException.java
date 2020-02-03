package com.joaopauloschmitz.algafoodapi.domain.exception;

public class GrupoNaoEncontradaException extends EntidadeNaoEncontradaException{

    public GrupoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public GrupoNaoEncontradaException(Long id) {
        this(String.format("Não existe um cadastro de grupo com código %d", id));
    }
}
