package br.ce.wcaquino.servicos;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {

	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
		
		Assert.assertEquals(5,calc.somar(1,8));
	}
	
}
