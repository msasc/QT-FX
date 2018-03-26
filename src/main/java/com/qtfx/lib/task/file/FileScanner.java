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

package com.qtfx.lib.task.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.task.TaskRun;
import com.qtfx.lib.util.Lists;

/**
 * A <code>FileScanner</code> scans the source directories and optionally their sub-directories, notifying each file or
 * directory found. It is implemented as a task to allow it to be executed in a separated thread with all the control
 * options of a task.
 *
 * @author Miquel Sas
 */
public class FileScanner extends TaskRun {

	/**
	 * The list of source directories or files to scan.
	 */
	private List<File> sourceFiles = new ArrayList<>();
	/**
	 * A boolean to indicate if sub-directories should be scanned.
	 */
	private boolean scanSubDirectories = true;
	/**
	 * A boolean to indicate if directories should be notified.
	 */
	private boolean notifyDirectories = true;
	/**
	 * A boolean to indicate if files should be notified.
	 */
	private boolean notifyFiles = true;
	/**
	 * The list of scanner listeners.
	 */
	private List<FileScannerListener> listeners = new ArrayList<>();
	/**
	 * The current source directory being scanned.
	 */
	private File currentSourceDirectory;

	/**
	 * Constructor.
	 */
	public FileScanner() {
		super();
	}

	/**
	 * Add a source directory or file to the list of source directories or files to scan.
	 * 
	 * @param file The source directory or file.
	 */
	public void addSource(File file) {
		sourceFiles.add(file);
	}

	/**
	 * Add a scanner listener.
	 * 
	 * @param listener The listener.
	 */
	public void addListener(FileScannerListener listener) {
		listeners.add(listener);
	}

	/**
	 * Returns a boolean indicating whether sub-directories should be scanned.
	 * 
	 * @return A boolean.
	 */
	public boolean isScanSubDirectories() {
		return scanSubDirectories;
	}

	/**
	 * Sets a boolean indicating whether sub-directories should be scanned.
	 * 
	 * @param scanSubDirectories A boolean.
	 */
	public void setScanSubDirectories(boolean scanSubDirectories) {
		this.scanSubDirectories = scanSubDirectories;
	}

	/**
	 * Returns a boolean indicating whether directories should be notified.
	 * 
	 * @return A boolean.
	 */
	public boolean isNotifyDirectories() {
		return notifyDirectories;
	}

	/**
	 * Sets a boolean indicating whether directories should be notified.
	 * 
	 * @param notifyDirectories A boolean.
	 */
	public void setNotifyDirectories(boolean notifyDirectories) {
		this.notifyDirectories = notifyDirectories;
	}

	/**
	 * Returns a boolean indicating whether files should be notified.
	 * 
	 * @return A boolean.
	 */
	public boolean isNotifyFiles() {
		return notifyFiles;
	}

	/**
	 * Sets a boolean indicating whether files should be notified.
	 * 
	 * @param notifyFiles A boolean.
	 */
	public void setNotifyFiles(boolean notifyFiles) {
		this.notifyFiles = notifyFiles;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double requestTotalWork() {
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void compute() throws Exception {

		// Check source files.
		for (File file : sourceFiles) {
			if (file.isFile()) {
				if (!file.exists()) {
					throw new IOException("Invalid source file " + file);
				}
			}
			if (file.isDirectory()) {
				if (!file.exists()) {
					throw new IOException("Invalid source directory " + file);
				}
			}
		}
		
		// The list with scanning files.
		List<File> files = new ArrayList<>();
		for (File file : sourceFiles) {
			files.add(file);
		}
		
		// Scan while files left.
		while (!files.isEmpty()) {
			
			// Check cancel.
			if (isCancelled()) {
				break;
			}
			
			// File does not exist, only if the the file has been removed when arriving at this point.
			if (!Lists.getLast(files).exists()) {
				Lists.removeLast(files);
				continue;
			}
			// If last file is a file, notify if applicable and remove it.
			if (Lists.getLast(files).isFile()) {
				if (isNotifyFiles()) {
					notifyFile(Lists.getLast(files));
				}
				Lists.removeLast(files);
				continue;
			}

			// If last file is a directory, first notify if applicable, remove it and if should scan subdirectories, add
			// the directory files.
			if (Lists.getLast(files).isDirectory()) {
				
				// Current source directory must be in the list of source files.
				if (sourceFiles.contains(Lists.getLast(files))) {
					currentSourceDirectory = Lists.getLast(files);
				}
				
				// Notify directory if applicable.
				if (isNotifyDirectories()) {
					notifyFile(Lists.getLast(files));
				}
				
				// Remove.
				File directory = Lists.removeLast(files);
				
				// Add children if should scan deeper.
				if (isScanSubDirectories()) {
					File[] children = directory.listFiles();
					if (children != null) {
						for (File child : children) {
							files.add(child);
						}
					}
				}
				
				continue;
			}
			
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIndeterminate() {
		return true;
	}

	/**
	 * Notify the file to listeners.
	 * 
	 * @param file The file to notify.
	 * @throws IOException If any error occurs when the listeners process the events.
	 */
	private void notifyFile(File file) throws IOException {
		for (FileScannerListener listener : listeners) {
			listener.file(currentSourceDirectory, file);
		}
	}

}
