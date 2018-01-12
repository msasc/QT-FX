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
package com.qtfx.lib.ml.data.mnist;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;

/**
 * Swing utilities. Since <code>javax.swing.SwingUtilities</code> has a private constructor and can not be extended,
 * what would be the natural way of adding functionality, the used methods of the swing class will be gradually
 * delegated, to avoid having the utility functionality distributed over several classes.
 * <p>
 * Simple methods like <i>invokeLater</i> will be simply copied, others more complicated will be delegated.
 * 
 * @author Miquel Sas
 */
public class SwingUtils {

	/**
	 * Sets the size of the window to a width and height factor of the screen size, and centers it on the screen.
	 * 
	 * @param window The window to set the size.
	 * @param widthFactor The width factor of the screen size.
	 * @param heightFactor The height factor of the screen size.
	 */
	public static void setSizeAndCenterOnSreen(Window window, double widthFactor, double heightFactor) {
		setSize(window, widthFactor, heightFactor);
		centerOnScreen(window);
	}

	/**
	 * Sets the size of the window to a width and height factor of the screen size.
	 * 
	 * @param window The window to set the size.
	 * @param widthFactor The width factor of the screen size.
	 * @param heightFactor The height factor of the screen size.
	 */
	public static void setSize(Window window, double widthFactor, double heightFactor) {
		Dimension screenSize = getScreenSize(window);
		int width = (int) (screenSize.getWidth() * widthFactor);
		int height = (int) (screenSize.getHeight() * heightFactor);
		window.setSize(width, height);
	}

	/**
	 * Centers the window on the screen.
	 * 
	 * @param window The window to center.
	 * @return The location or top-left corner.
	 */
	public static Point centerOnScreen(Window window) {
		Dimension screenSize = getScreenSize(window);
		Dimension windowSize = window.getSize();
		int x = (int) ((screenSize.getWidth() - windowSize.getWidth()) / 2);
		int y = (int) ((screenSize.getHeight() - windowSize.getHeight()) / 2);
		window.setLocation(x, y);
		return window.getLocation();
	}

	/**
	 * Returns the graphics device that should apply to a window.
	 * 
	 * @param window The window.
	 * @return The graphics device.
	 */
	public static GraphicsDevice getGraphicsDevice(Window window) {
		if (window != null) {
			return window.getGraphicsConfiguration().getDevice();
		}
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	}

	/**
	 * Returns the size of the screen containing the argument window or the primary screen if current window is not
	 * selected.
	 * 
	 * @param window The window.
	 * @return The screen size.
	 */
	public static Dimension getScreenSize(Window window) {
		return getGraphicsDevice(window).getConfigurations()[0].getBounds().getSize();
	}

	/**
	 * Returns the position of the screen containing the argument window or the primary screen if current window is not
	 * selected.
	 * 
	 * @param window The window.
	 * @return The location point.
	 */
	public static Point getScreenPosition(Window window) {
		GraphicsDevice graphicsDevice = getGraphicsDevice(window);
		GraphicsConfiguration[] configurations = graphicsDevice.getConfigurations();
		return configurations[0].getBounds().getLocation();
	}

	/**
	 * Gets a dimension applying a width and/or height factor relative to the screen dimension.
	 * 
	 * @param window The window.
	 * @param widthFactor The width factor relative to the screen (0 &lt; factor &lt;= 1).
	 * @param heightFactor The height factor relative to the screen (0 &lt; factor &lt;= 1).
	 * @return The screen dimension.
	 */
	public static Dimension factorScreenDimension(Window window, double widthFactor, double heightFactor) {
		Dimension d = getScreenSize(window);
		d.width *= widthFactor;
		d.height *= heightFactor;
		return d;
	}

	/**
	 * Locates a dimension on the screen being the left space the width factor of difference between the screen size and
	 * the dimension. A value of 0 moves the dimension to the left, while a value of 1 moves the dimension to the right.
	 * The same applies to the height.
	 * 
	 * @param window The window.
	 * @param factorWidth Width factor.
	 * @param factorHeight Height factor.
	 * @return The top-left corner point.
	 */
	public static Point moveWindowOnScreen(Window window, double factorWidth, double factorHeight) {
		Dimension sz = window.getSize();
		Dimension szScreen = getScreenSize(window);
		Point pt = getScreenPosition(window);
		if (szScreen.width > sz.width) {
			pt.x = pt.x + (int) ((szScreen.width - sz.width) * factorWidth);
			pt.y = pt.y + (int) ((szScreen.height - sz.height) * factorHeight);
		}
		return pt;
	}

}
