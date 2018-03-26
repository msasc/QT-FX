package com.qtfx;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class TestLocale {
	
	static class LocaleComparator implements Comparator<Locale> {
		@Override
		public int compare(Locale o1, Locale o2) {
			return o1.toString().compareTo(o2.toString());
		}
		
	}

	public static void main(String[] args) {
		Locale[] locales = Locale.getAvailableLocales();
		Arrays.sort(locales, new LocaleComparator());
		for (Locale locale : locales) {
			System.out.println(locale);
		}
	}

}
