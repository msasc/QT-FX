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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Lists this library icon path names.
 * 
 * @author Miquel Sas
 */
public interface Icons {
	
	static ImageView get(String resource) throws IOException {
		File file = new File(resource);
		FileInputStream fi = new FileInputStream(file);
		Image image = new Image(fi);
		return new ImageView(image);
	}
	
	/** Common application icons 32x32 */
	
	String APP_32x32_DIALOG_ERROR = "resources/images/app/32x32/dialog-error.png";
	String APP_32x32_DIALOG_INFORMATION = "resources/images/app/32x32/dialog-information.png";
	String APP_32x32_DIALOG_CONFIRM = "resources/images/app/32x32/dialog-confirm.png";
	String APP_32x32_DIALOG_WARNING = "resources/images/app/32x32/dialogwarning.png";
	
}
