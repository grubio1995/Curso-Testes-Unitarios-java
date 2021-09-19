package br.ce.wcaquino.servicos;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class CalculadoraMockTest {

	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		when(calc.somar(argCapt.capture(),argCapt.capture())).thenReturn(5);
		
		Assert.assertEquals(5,calc.somar(134435,-234));
		System.out.println(argCapt.getAllValues());

	}
	
}
