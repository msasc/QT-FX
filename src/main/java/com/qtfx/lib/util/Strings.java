/*
 * Copyright (C) 2017 Miquel Sas
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.qtfx.lib.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * String utilities.
 *
 * @author Miquel Sas
 */
public class Strings extends org.apache.commons.lang3.StringUtils {

	/** Sample list of digits to generate random digits. */
	public static final String DIGITS = "0123456789";
	/** Sample list of letters to generate random letters. */
	public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * Returns a random char within the source string.
	 * 
	 * @param source The source string.
	 * @return The random char.
	 */
	public static char getRandomChar(String source) {
		int index = Random.nextInt(source.length());
		return source.charAt(index);
	}

	/**
	 * Returns the first string not null, or an empty string.
	 * 
	 * @param strings The list of strings.
	 * @return The first string not null, or an empty string.
	 */
	public static String getFirstNotNull(String... strings) {
		StringBuilder b = new StringBuilder();
		for (String s : strings) {
			if (s != null) {
				b.append(s);
				break;
			}
		}
		return b.toString();
	}

	/**
	 * Check if the string is contained in the list of options.
	 * 
	 * @param string The source string.
	 * @param options The list of options.
	 * @return A boolean.
	 */
	public static boolean in(String string, String... options) {
		for (String option : options) {
			if (option.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the string is contained in the list of options, case insensitive.
	 * 
	 * @param string The source string.
	 * @param options The list of options.
	 * @return A boolean.
	 */
	public static boolean inNoCase(String string, String... options) {
		string = string.toLowerCase();
		for (String option : options) {
			if (option.toLowerCase().equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Parse a string
	 * 
	 * @param string The string to parse.
	 * @param separator The separator.
	 * @return the array of tokens
	 */
	public static String[] parse(String string, String separator) {
		StringTokenizer tokenizer = new StringTokenizer(string, separator);
		ArrayList<String> list = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken().trim());
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Parse and capitalize.
	 * 
	 * @param srcStr Source string.
	 * @param srcSep Source separator.
	 * @param dstSep Destination separator.
	 * @return The capitalized string.
	 */
	public static String parseCapitalize(String srcStr, String srcSep, String dstSep) {
		StringBuilder b = new StringBuilder();
		String[] words = parse(srcStr, srcSep);
		for (int i = 0; i < words.length; i++) {
			if (i > 0) {
				b.append(dstSep);
			}
			b.append(capitalize(words[i]));
		}
		return b.toString();
	}

	/**
	 * Return the number of lines.
	 * 
	 * @param str The string.
	 * @return The number of lines.
	 */
	public static int countLines(String str) {
		return countMatches(str, '\n');
	}

	/**
	 * Return the offset of the end of the given line, not including the line separator character.
	 * @param str The string.
	 * @param line The line number.
	 * @return The offset or -1 if line number is greater that count lines.
	 */
	public static int offsetEndLine(String str, int line) {
		int count = 0;
		int offset = -1;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\n') {
				count++;
			}
			if (count == line) {
				offset = i + 1;
				break;
			}
		}
		// Last line not ended.
		if (offset == -1 && count == line - 1) {
			offset = str.length();
		}
		return offset;
	}

	public static int offsetStartLine(String str, int line) {
		int offsetEnd = offsetEndLine(str, line);
		int offsetStart = str.lastIndexOf('\n', offsetEnd);
		return (offsetStart == -1 ? 0 : offsetStart);
	}
}
