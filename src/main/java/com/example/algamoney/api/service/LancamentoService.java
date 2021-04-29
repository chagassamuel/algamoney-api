package com.example.algamoney.api.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.exception.PessoaInexistenteOuInativaException;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.filter.LancamentoFilter;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaService pessoaService;

	public Lancamento buscarPeloCodigo(Long codigo) {
		Lancamento lancamentoSalvo = this.lancamentoRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return lancamentoSalvo;
	}

	public List<Lancamento> buscarTodos() {
		return lancamentoRepository.findAll();
	}

	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter) {
		return lancamentoRepository.filtrar(lancamentoFilter);
	}

	public Lancamento salvar(Lancamento lancamento) {
		Pessoa pessoa = pessoaService.buscarPessoaPeloCodigo(lancamento.getPessoa());
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}

		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		return lancamentoSalvo;
	}

	public void excluir(Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarPeloCodigo(codigo);
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		return lancamentoRepository.save(lancamentoSalvo);
	}

}