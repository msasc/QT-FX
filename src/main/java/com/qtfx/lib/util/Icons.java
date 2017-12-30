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

package com.qtfx.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Lists this library icon path names.
 * 
 * @author Miquel Sas
 */
public interface Icons {

	/** Logger instance. */
	Logger LOGGER = LogManager.getLogger();

	/**
	 * Return the resource.
	 * 
	 * @param resource The resource path.
	 * @return The image view.
	 */
	static ImageView get(String resource) {
		try {
			File file = new File(resource);
			FileInputStream fi = new FileInputStream(file);
			Image image = new Image(fi);
			return new ImageView(image);
		} catch (IOException exc) {
			LOGGER.catching(exc);
			return null;
		}
	}

	/////////////////////////////////
	// Common application icons 32x32

	String APP_32x32_DIALOG_ERROR = "resources/images/app/32x32/dialog-error.png";
	String APP_32x32_DIALOG_INFORMATION = "resources/images/app/32x32/dialog-information.png";
	String APP_32x32_DIALOG_CONFIRMATION = "resources/images/app/32x32/dialog-confirm.png";
	String APP_32x32_DIALOG_WARNING = "resources/images/app/32x32/dialog-warning.png";

	///////////////////////////////////
	// Very flat and simple icons 24x24
	
	String FLAT_24x24_CANCEL = "resources/images/flat/24x24/cancel.png";
	String FLAT_24x24_CLOSE = "resources/images/flat/24x24/close.png";
	String FLAT_24x24_INFO = "resources/images/flat/24x24/info.png";
	String FLAT_24x24_PAUSE = "resources/images/flat/24x24/pause.png";
	String FLAT_24x24_EXECUTE = "resources/images/flat/24x24/resume.png";
	
	///////////////////////////////////
	// Very flat and simple icons 16x16
	
	String FLAT_16x16_CANCEL = "resources/images/flat/16x16/cancel.png";
	String FLAT_16x16_CLOSE = "resources/images/flat/16x16/close.png";
	String FLAT_16x16_INFO = "resources/images/flat/16x16/info.png";
	String FLAT_16x16_PAUSE = "resources/images/flat/16x16/pause.png";
	String FLAT_16x16_EXECUTE = "resources/images/flat/16x16/resume.png";
	
	///////////////
	// Chart icons.
	
	String CHART_16x16_TITLEBAR_CLOSE = "resources/images/chart/16x16/titlebar_close.png";
	
}
