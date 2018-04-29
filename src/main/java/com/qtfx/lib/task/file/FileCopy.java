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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.qtfx.lib.app.Session;
import com.qtfx.lib.task.State;
import com.qtfx.lib.task.Task;
import com.qtfx.lib.util.Files;

/**
 * Utility class to copy files from a list of source directories to a list of destination directories.
 *
 * @author Miquel Sas
 */
public class FileCopy extends Task {

	/**
	 * Count listener.
	 */
	class CountListener implements FileScannerListener {
		/** A boolean that indicated if the count if for purge. */
		private boolean countForPurge = false;
		/** Number of files to process. */
		private long count = 0;

		/**
		 * Constructor indicating if the count is for countForPurge.
		 * 
		 * @param countForPurge A boolean.
		 */
		public CountListener(boolean countForPurge) {
			super();
			this.countForPurge = countForPurge;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void file(File sourceDirectory, File file) throws IOException {

			// Display file in the from label.
			updateMessage(LABEL_FROM, file.toString());

			// Check exclude.
			if (countForPurge) {
				if (isExcludePurge(file)) {
					return;
				}
			} else {
				if (isExcludeCopy(file)) {
					return;
				}
			}

			// Cumulate bytes to copy.
			if (!countForPurge && file.isFile()) {
				bytesToProcess += file.length();
			}

			count++;
		}

		/**
		 * Returns the number of files to process.
		 * 
		 * @return The counted number of files.
		 */
		public long getCount() {
			return count;
		}
	}

	/**
	 * Copy listener.
	 */
	class CopyListener implements FileScannerListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void file(File sourceDirectory, File file) throws IOException {

			// Check exclude.
			if (isExcludeCopy(file)) {
				return;
			}

			// Notify step start.
			long workDone = (long) (getWorkDone() + 1);
			long totalWork = (long) getTotalWork();

			StringBuilder msgProc = new StringBuilder();
			msgProc.append(Files.getSizeLabel(bytesProcessed + file.length(), 1, getSession().getLocale()));
			msgProc.append(" / ");
			msgProc.append(Files.getSizeLabel(bytesToProcess, 1, getSession().getLocale()));
			update(msgProc.toString(), workDone, totalWork);

			// Copy the source file.
			if (file.isFile()) {

				// Take the appropriate destination file. First check direct file copy, then build the destination file
				// using source and destination directories.
				File destinationFile = mapFiles.get(file);
				if (destinationFile == null) {
					// The destination root directory.
					File destinationDirectory = mapDirectories.get(sourceDirectory);
					// The destination file.
					destinationFile = Files.getDestinationFile(sourceDirectory, file, destinationDirectory);
				}

				String from = getSession().getString("tokenFrom") + ": " + file.getAbsolutePath();
				String to = getSession().getString("tokenTo") + ": " + destinationFile.getAbsolutePath();
				updateMessage(LABEL_FROM, from);
				updateMessage(LABEL_TO, to);

				// If destination file exists and can not write,would normally be by access denied. Try delete
				// first.
				if (destinationFile.exists()) {
					if (!destinationFile.canWrite()) {
						if (!destinationFile.delete()) {
							StringBuilder msgExc = new StringBuilder();
							msgExc.append(getSession().getString("securityAccessToFileDenied"));
							msgExc.append(" -> ");
							msgExc.append(file);
							throw new IOException(msgExc.toString());
						}
					}
				}

				// Try copy without any timestamp check.
				Files.copy(file, destinationFile);

				// Cumulate bytes processed.
				bytesProcessed += file.length();
			}

		}
	}

	/**
	 * Purge listener.
	 */
	class PurgeListener implements FileScannerListener {

		/** Stack to store directories not empty. */
		private Deque<File> deque = new ArrayDeque<>();

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void file(File destinationDirectory, File file) throws IOException {

			// Check exclude.
			if (isExcludePurge(file)) {
				return;
			}

			// Notify step start.
			// Notify step start.
			long workDone = (long) (getWorkDone() + 1);
			long totalWork = (long) getTotalWork();

			update("Purge destination ", workDone, totalWork);

			// If the file is a directory, just add it to the deque.
			if (file.isDirectory()) {
				deque.addLast(file);
			}

			// If it is a file, try countForPurge.
			if (file.isFile()) {
				String purge = getSession().getString("tokenPurge") + ": " + file.getAbsolutePath();
				updateMessage(LABEL_FROM, purge);
				purge(destinationDirectory, file);
			}

			// Files in the stack are directories. While last directories are empty delete them.
			while (!deque.isEmpty()) {
				if (Files.isEmpty(deque.getLast())) {
					File last = deque.removeLast();
					String purge = getSession().getString("tokenPurge") + ": " + last.getAbsolutePath();
					updateMessage(LABEL_FROM, purge);
					last.delete();
				} else {
					break;
				}
			}
		}
	}

	/**
	 * Additional label to show the from file.
	 */
	private static final String LABEL_FROM = "From";
	/**
	 * Additional label to show the to file.
	 */
	private static final String LABEL_TO = "To";

	/**
	 * The map of source and destination directories.
	 */
	private Map<File, File> mapDirectories = new LinkedHashMap<>();
	/**
	 * The map of source and destination files.
	 */
	private Map<File, File> mapFiles = new LinkedHashMap<>();
	/**
	 * List of source files (sub-directories or files) to exclude from copy.
	 */
	private List<File> excludeCopy = new ArrayList<>();
	/**
	 * List of destination files (sub-directories or files) to exclude from countForPurge.
	 */
	private List<File> excludePurge = new ArrayList<>();

	/**
	 * A boolean that indicates whether sub-directories from the source should be processed.
	 */
	private boolean processSubDirectories = true;
	/**
	 * A boolean that indicates whether destination should be purged, that is, non existing files in the source deleted.
	 */
	private boolean purgeDestination = false;

	/**
	 * Counter for bytes to process.
	 */
	private long bytesToProcess;
	/**
	 * Counter for bytes processed.
	 */
	private long bytesProcessed;
	/**
	 * Current running scanner.
	 */
	private FileScanner scanner;

	/**
	 * Constructor.
	 * 
	 * @param session The working session.
	 */
	public FileCopy(Session session) {
		super(session);
		addMessage(LABEL_FROM);
		addMessage(LABEL_TO);
	}

	/**
	 * Add source and destination directories to be copied.
	 * 
	 * @param sourceDirectory The source directory.
	 * @param destinationDirectory The destination directory.
	 */
	public void addDirectories(File sourceDirectory, File destinationDirectory) {
		mapDirectories.put(sourceDirectory, destinationDirectory);
	}

	/**
	 * Add source and destination files to be copied.
	 * 
	 * @param sourceFile The source file.
	 * @param destinationFile The destination file.
	 */
	public void addFiles(File sourceFile, File destinationFile) {
		mapFiles.put(sourceFile, destinationFile);
	}

	/**
	 * Exclude the argument source file or directory from the copy process.
	 * 
	 * @param source The source to exclude.
	 */
	public void addExcludeCopy(File source) {
		if (!excludeCopy.contains(source)) {
			excludeCopy.add(source);
		}
	}

	/**
	 * Exclude the argument destination file or directory from the countForPurge process.
	 * 
	 * @param destination The destination to exclude.
	 */
	public void addExcludePurge(File destination) {
		if (!excludePurge.contains(destination)) {
			excludePurge.add(destination);
		}
	}

	/**
	 * Check whether the source file must be excluded from copy.
	 * 
	 * @param source The source file.
	 * @return A boolean.
	 */
	private boolean isExcludeCopy(File source) {
		for (File file : excludeCopy) {
			if (Files.isParentAbsolute(file, source)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the destination file must be excluded from countForPurge.
	 * 
	 * @param destination The destination file.
	 * @return A boolean.
	 */
	private boolean isExcludePurge(File destination) {
		for (File file : excludePurge) {
			if (Files.isParentAbsolute(file, destination)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a boolean indicating whether sub-directories from the source should be processed.
	 * 
	 * @return A boolean.
	 */
	public boolean isProcessSubDirectories() {
		return processSubDirectories;
	}

	/**
	 * Returns a boolean that indicates whether destination should be purged.
	 * 
	 * @return A boolean.
	 */
	public boolean isPurgeDestination() {
		return purgeDestination;
	}

	/**
	 * Sets a boolean indicating whether sub-directories from the source should be processed.
	 * 
	 * @param processSubDirectories A boolean.
	 */
	public void setProcessSubDirectories(boolean processSubDirectories) {
		this.processSubDirectories = processSubDirectories;
	}

	/**
	 * Sets a boolean that indicates whether destination should be purged.
	 * 
	 * @param purgeDestination A boolean.
	 */
	public void setPurgeDestination(boolean purgeDestination) {
		this.purgeDestination = purgeDestination;
	}

	/**
	 * Returns the source directory given the destination one, in the list of source-destination directories.
	 * 
	 * @param destinationDirectory The destination directory.
	 * @return The source directory.
	 */
	private File getSourceDirectory(File destinationDirectory) {
		Iterator<File> keys = mapDirectories.keySet().iterator();
		while (keys.hasNext()) {
			File source = keys.next();
			File destination = mapDirectories.get(source);
			if (destination.equals(destinationDirectory)) {
				return source;
			}
		}
		return null;
	}

	/**
	 * Purge the destination file if not exists in the source.
	 * 
	 * @param destinationDirectory Destination directory.
	 * @param destinationFile Destination file.
	 * @throws IOException If an IO error occurs.
	 */
	private void purge(File destinationDirectory, File destinationFile) throws IOException {
		File sourceDirectory = getSourceDirectory(destinationDirectory);
		File sourceFile = Files.getSourceFile(destinationDirectory, destinationFile, sourceDirectory);
		if (!sourceFile.exists()) {
			destinationFile.delete();
		}
	}

	/**
	 * Returns the source scanner.
	 * 
	 * @return The scanner.
	 */
	private FileScanner getScanner() {
		FileScanner scanner = new FileScanner(getSession());
		scanner.setScanSubDirectories(isProcessSubDirectories());
		Iterator<File> keysDirectories = mapDirectories.keySet().iterator();
		while (keysDirectories.hasNext()) {
			File source = keysDirectories.next();
			scanner.addSource(source);
		}
		Iterator<File> keysFiles = mapFiles.keySet().iterator();
		while (keysFiles.hasNext()) {
			File source = keysFiles.next();
			scanner.addSource(source);
		}
		return scanner;
	}

	/**
	 * Returns the scanner for purge (destination directories).
	 * 
	 * @return The scanner for purge.
	 */
	private FileScanner getScannerForPurge() {
		FileScanner scanner = new FileScanner(getSession());
		scanner.setScanSubDirectories(isProcessSubDirectories());
		Iterator<File> keys = mapDirectories.keySet().iterator();
		while (keys.hasNext()) {
			File source = keys.next();
			scanner.addSource(mapDirectories.get(source));
		}
		return scanner;
	}

	/**
	 * Calculate and register the total work.
	 * 
	 * @return A boolean indicating whether the counting process was cancelled.
	 * @throws Exception
	 */
	private boolean calculateTotalWork() throws Exception {
		
		// Number of steps and bytes to process.
		long count = 0;
		bytesToProcess = 0;

		
		// If should countForPurge, count to analyze countForPurge.
		if (isPurgeDestination()) {

			// The scanner to count the destination.
			scanner = getScannerForPurge();

			// The counter listener.
			CountListener counterListener = new CountListener(true);
			scanner.addListener(counterListener);

			// Do scan.
			scanner.compute();
			if (scanner.isCancelled()) {
				return false;
			}

			// Read steps.
			count += counterListener.getCount();
		}

		// Clear labels.
		clearMessages();

		// The scanner to count the source.
		scanner = getScanner();

		// The counter listener.
		CountListener counterListener = new CountListener(false);
		scanner.addListener(counterListener);

		// Do scan.
		scanner.compute();
		if (scanner.isCancelled()) {
			clearMessages();
			return false;
		}

		// Read steps.
		count += counterListener.getCount();

		// Clear labels.
		clearMessages();

		// Register total work.
		setTotalWork(count);
		return true;

	}
	
	/**
	 * Clear additional messages (FROM and TO)
	 */
	private void clearMessages() {
		updateMessage(LABEL_FROM, "");
		updateMessage(LABEL_TO, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void compute() throws Exception {
		
		// Reset bytes processed.
		bytesProcessed = 0;
		
		// Count.
		updateCounting();
		if (!calculateTotalWork()) {
			return;
		}
		
		// If purge required, do it.
		if (isPurgeDestination()) {
			// Clear messages.
			clearMessages();

			// Scanner to purge.
			scanner = getScannerForPurge();
			scanner.addListener(new PurgeListener());
			
			// Do work.
			scanner.compute();
			if (scanner.isCancelled()) {
				clearMessages();
				setState(State.CANCELLED);
				return;
			}
		}
		
		// Copy process.
		clearMessages();
		
		// Scanner to copy.
		scanner = getScanner();
		scanner.addListener(new CopyListener());
		
		// Do work.
		scanner.compute();
		if (scanner.isCancelled()) {
			setState(State.CANCELLED);
		}
		
		clearMessages();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIndeterminate() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if (scanner != null) {
			scanner.cancel(mayInterruptIfRunning);
		}
		return super.cancel(mayInterruptIfRunning);
	}

}
