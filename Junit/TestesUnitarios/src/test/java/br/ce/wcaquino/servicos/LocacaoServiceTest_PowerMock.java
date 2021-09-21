package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersPropios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocacaoServiceTest_PowerMock {

	@InjectMocks
	private LocacaoService service;
	@Mock
	private SPCService spc;
	@Mock
	private EmailService email;
	@Mock
	private LocacaoDAO dao;
	
	private List<Filme> filmes = new ArrayList<Filme>();

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		filmes = new ArrayList<Filme>();
		service = PowerMockito.spy(service);
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		filmes = Arrays.asList(umFilme().comValor(5.0).agora());

		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(24, 9, 2021));
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(24, 9, 2021)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(25, 9, 2021)), is(true));


	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		
		// cenario
		Usuario usuario = umUsuario().agora();
		filmes = Arrays.asList(umFilme().agora());

		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(25, 9, 2021));
 		
		// acao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}
	
	
	@Test
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		//cenário
		Usuario usuario = umUsuario().agora();	
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		PowerMockito.doReturn(1.0).when(service,"calcularValorLocacao", filmes);
				
		//ação
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificação
		Assert.assertThat(locacao.getValor(),is(1.0));
		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao",filmes);
	}
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenário
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//ação
		Double valor = (Double) Whitebox.invokeMethod(service,"calcularValorLocacao",filmes);
		
		//verificação
		Assert.assertThat(valor, is(4.0));		
		
	}
}
