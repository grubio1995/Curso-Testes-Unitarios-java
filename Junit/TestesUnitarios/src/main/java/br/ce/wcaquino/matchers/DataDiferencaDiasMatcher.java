package br.ce.wcaquino.matchers;

import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

	private Integer diferencaDeDias;

	public DataDiferencaDiasMatcher(Integer diferencaDeDias) {
		this.diferencaDeDias = diferencaDeDias;
	}

	public void describeTo(Description desc) {
		Date date = DataUtils.obterDataComDiferencaDias(diferencaDeDias);
		String esperado = "A diferença de " + diferencaDeDias + " dia(s) era para ser: " + date.toString();
		desc.appendText(esperado);
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(diferencaDeDias));
	}
}
