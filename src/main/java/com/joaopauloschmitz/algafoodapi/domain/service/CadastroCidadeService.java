package com.joaopauloschmitz.algafoodapi.domain.service;

import com.joaopauloschmitz.algafoodapi.domain.exception.CidadeNaoEncontradaException;
import com.joaopauloschmitz.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.joaopauloschmitz.algafoodapi.domain.model.Cidade;
import com.joaopauloschmitz.algafoodapi.domain.model.Estado;
import com.joaopauloschmitz.algafoodapi.domain.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCidadeService {

    public static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso";

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @Transactional
    public Cidade salvar(Cidade cidade) {
        Long estadoId = cidade.getEstado().getId();

        Estado estado = this.cadastroEstadoService.buscarOuFalhar(estadoId);

        cidade.setEstado(estado);
        return this.cidadeRepository.save(cidade);
    }

    @Transactional
    public void excluir(Long id) {
        try {
            this.cidadeRepository.deleteById(id);
            this.cidadeRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new CidadeNaoEncontradaException(id);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, id));
        }
    }

    public Cidade buscarOuFalhar(Long id) {
        return this.cidadeRepository.findById(id).orElseThrow(
                () -> new CidadeNaoEncontradaException(id)
        );
    }
}
