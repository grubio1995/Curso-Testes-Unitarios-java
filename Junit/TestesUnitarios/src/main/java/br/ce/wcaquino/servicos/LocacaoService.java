package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.servicos.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {

	private LocacaoDAO dao;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

		if (usuario == null) {
			throw new LocadoraException("Usuário vazio");
		}

		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0d;

		for (int i = 0; i < filmes.size(); i++) {

			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();

			switch (i) {
			case 2:
				valorFilme = valorFilme * 0.75; // 25% de desconto
				break;
			case 3:
				valorFilme = valorFilme * 0.50; // 50% de desconto
				break;
			case 4:
				valorFilme = valorFilme * 0.25; // 75% de desconto
				break;
			case 5:
				valorFilme = valorFilme - valorFilme; // 100% de desconto
				break;
			}

			valorTotal += valorFilme;

		}

		locacao.setValor(valorTotal);

		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY))
			dataEntrega = adicionarDias(dataEntrega, 1);

		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		dao.salvar(locacao);

		return locacao;
	}

}