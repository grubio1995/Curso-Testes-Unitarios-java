package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

public class OrdemTest {
	
	public static int contador = 0;
	
	public void inicia() {
		contador = 1;
	}

	public void verifica() {
		Assert.assertEquals(1, contador);
	}
	
	@Test
	public void testGeral() {
		inicia();
		verifica();
	}
	
}
