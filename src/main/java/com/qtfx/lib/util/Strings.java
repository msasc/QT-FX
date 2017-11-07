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

import org.apache.commons.lang3.StringUtils;

/**
 * String utilities.
 *
 * @author Miquel Sas
 */
public class Strings extends StringUtils {
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
}
