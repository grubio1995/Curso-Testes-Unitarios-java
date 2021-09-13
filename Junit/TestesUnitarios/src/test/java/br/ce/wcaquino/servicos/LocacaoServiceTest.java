package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.matchers.MatchersPropios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersPropios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersPropios.ehHojeComDiferencaDeDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
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
//		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cenario
		Usuario usuario = new Usuario("Usu�rio 1");
		filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verifica��o
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getDataLocacao(),ehHoje());
		error.checkThat(locacao.getDataRetorno(),ehHojeComDiferencaDeDias(1));
	
	}

	@Test(expected = FilmeSemEstoqueException.class) // forma elegante
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {

		// cenario
		Usuario usuario = new Usuario("Usu�rio 1");
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
			assertThat(e.getMessage(), is("Usu�rio vazio"));
		}

	}

	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemFime() throws FilmeSemEstoqueException, LocadoraException { // forma
																											// nova

		// cenario
		Usuario usuario = new Usuario("Usu�rio 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// acao
		service.alugarFilme(usuario, null);

	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cenario
		Usuario usuario = new Usuario("Usu�rio 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0));

		// acao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

	}
}
