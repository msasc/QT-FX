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

package com.qtfx.lib.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface responsible to read/write objects without the restrictions a class path refatoring.
 *
 * @author Miquel Sas
 */
public interface ObjectIO {

	/**
	 * Read the object from an input stream.
	 * 
	 * @param is The input stream
	 * @return The object.
	 * @throws IOException
	 */
	Object read(InputStream is) throws IOException;
	
	/**
	 * Write the object.
	 * @param os The output stream
	 * @param obj The object.
	 * @throws IOException
	 */
	void write(OutputStream os, Object obj) throws IOException;
}
