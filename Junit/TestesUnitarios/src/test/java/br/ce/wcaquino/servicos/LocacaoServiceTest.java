package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Test
	public void teste() { 

		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuário 1");
		Filme filme = new Filme("Filme 1", 2, 6.0);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filme); 
		 
		//verificação
		error.checkThat(locacao.getValor(),is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(),new Date()),is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(),obterDataComDiferencaDias(1)),is(false));

	}	

}