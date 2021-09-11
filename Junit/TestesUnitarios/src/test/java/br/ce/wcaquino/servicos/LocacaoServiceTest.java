package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.servicos.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;

	private List<Filme> filmes = new ArrayList<Filme>();

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {

		service = new LocacaoService();

		filmes = new ArrayList<Filme>();
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// cenario
		Usuario usuario = new Usuario("Usuário 1");
		filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

	}

	@Test(expected = FilmeSemEstoqueException.class) // forma elegante
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {

		// cenario
		Usuario usuario = new Usuario("Usuário 1");
		filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));

		// acao
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemUsuario() throws FilmeSemEstoqueException { // forma robusta

		// cenario
		filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

		// acao
		try {

			service.alugarFilme(null, filmes);
			Assert.fail();

		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário vazio"));
		}

	}

	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemFime() throws FilmeSemEstoqueException, LocadoraException { // forma
																												// nova

		// cenario
		Usuario usuario = new Usuario("Usuário 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// acao
		service.alugarFilme(usuario, null);

	}

	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuário 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Fime 2", 2, 4.0),
				new Filme("Fime 3", 2, 4.0));

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verificacao
		// 4 + 4 + 3
		assertThat(resultado.getValor(), is(11.0));

	}

	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuário 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Fime 2", 2, 4.0),
				new Filme("Fime 3", 2, 4.0), new Filme("Fime 4", 2, 4.0));

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verificacao
		// 4 + 4 + 3 + 2
		assertThat(resultado.getValor(), is(13.0));

	}

	@Test
	public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuário 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Fime 2", 2, 4.0),
				new Filme("Fime 3", 2, 4.0), new Filme("Fime 4", 2, 4.0), new Filme("Fime 5", 2, 4.0));

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verificacao
		// 4 + 4 + 3 + 2 + 1
		assertThat(resultado.getValor(), is(14.0));

	}

	public void naodevePagarNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Usuário 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Fime 2", 2, 4.0),
				new Filme("Fime 3", 2, 4.0), new Filme("Fime 4", 2, 4.0), new Filme("Fime 5", 2, 4.0),
				new Filme("Fime 6", 2, 4.0));

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verificacao
		// 4 + 4 + 3 + 2 + 1 + 0
		assertThat(resultado.getValor(), is(14.0));

	}
	
	@Test
	public void deveDevolverNaSegunbdaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// cenario
		Usuario usuario = new Usuario("Usuário 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0));
		
		// acao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// verificacao
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		assertTrue(ehSegunda);

	}
}
