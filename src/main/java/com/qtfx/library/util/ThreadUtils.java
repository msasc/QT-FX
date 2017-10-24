/*
 * Copyright (C) 2015 Miquel Sas
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

package com.qtfx.library.util;

import java.awt.EventQueue;
import java.util.Collection;

import javafx.application.Platform;

/**
 * Thread utilities.
 * 
 * @author Miquel Sas
 */
public class ThreadUtils extends org.apache.commons.lang3.ThreadUtils {

	/**
	 * A boolean that indicates whether when the FX application thread was running. This is important in the moment to
	 * update observable properties.
	 */
	private static Boolean fxApplicationThread = null;
	
	static {
		if (fxApplicationThread == null) {
			fxApplicationThread = false;
			Collection<Thread> threads = getAllThreads();
			for (Thread thread : threads) {
				if (thread.getName() != null && thread.getName().startsWith("JavaFX")) {
					fxApplicationThread = true;
					break;
				}
			}
		}
	}

	public static void runLater(Runnable r) {
		if (fxApplicationThread) {
			Platform.runLater(r);
		} else {
			EventQueue.invokeLater(r);
		}
	}

}
