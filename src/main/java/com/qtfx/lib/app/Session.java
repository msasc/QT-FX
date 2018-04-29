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

package com.qtfx.lib.app;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

import com.qtfx.lib.util.Files;
import com.qtfx.lib.util.SystemUtils;

/**
 * A session manages the features related to one user executing one instance of the system. The selected locale, the
 * user and its rights, all are features packaged in the session.
 * <p>
 * Wherever a locale, user rights of any other user feature, the session will be required.
 * <p>
 * In a swing application, one session will be created at start time after logon, and this will be the unique session
 * managed by the application.
 * <p>
 * In a <i>WEB</i> application, normally under the execution of an <i>HttpSevlet</i>, mainly using the session of an
 * <i>HttpServelRequest</i>, many sessions will be available, each one corresponding the every detached request.
 * 
 * @author Miquel Sas
 */
public class Session {

	/**
	 * Default static session.
	 */
	private static Session session;

	/**
	 * Return the default static session.
	 * 
	 * @return The default session.
	 */
	public static Session getSession() {
		if (session == null) {
			session = US;
		}
		return session;
	}

	/**
	 * Set the default session.
	 * 
	 * @param session The default session.
	 */
	public static void setSession(Session session) {
		Session.session = session;
	}

	/**
	 * Statically access a string when trhere is only a locale available.
	 * 
	 * @param key The string key.
	 * @param locale The locale.
	 * @return The string.
	 */
	public static String getString(String key, Locale locale) {
		return TextServer.getString(key, locale);
	}

	/**
	 * Adds a base resource to the list of base resources.
	 * 
	 * @param fileName The base resource to add.
	 */
	public static void addBaseResource(String fileName) {
		TextServer.addBaseResource(fileName);
	}

	/**
	 * Default US session.
	 */
	public static Session US = new Session(Locale.US);

	/** The working locale. */
	private Locale locale;
	/** The user logged in the session. */
	private User user;
	/** Security manager. */
	private Security security;

	/**
	 * Constructor.
	 * 
	 * @param locale The session locale.
	 */
	public Session(Locale locale) {
		super();
		setLocale(locale);
	}

	/**
	 * Returns the session locale.
	 * 
	 * @return The session locale.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Set the session locale. Must be set at start up and does not trigger any interface update.
	 * 
	 * @param locale The session locale.
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Returns the working user.
	 * 
	 * @return The working user.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the working user.
	 * 
	 * @param user The working user.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Returns the security manager.
	 * 
	 * @return The security manager.
	 */
	public Security getSecurity() {
		return security;
	}

	/**
	 * Sets the security manager.
	 * 
	 * @param security The security manager.
	 */
	public void setSecurity(Security security) {
		this.security = security;
	}

	/**
	 * Returns the access mode for the argument key.
	 * 
	 * @param accessKey The access key.
	 * @return The access mode or not defined.
	 */
	public AccessMode getAccessMode(String accessKey) {
		if (getUser() != null && getSecurity() != null && accessKey != null) {
			return getSecurity().getAccessMode(getUser(), accessKey);
		}
		return AccessMode.NOT_DEFINED;
	}

	/**
	 * Returns a keyed and localized string.
	 * 
	 * @param key The key.
	 * @return The localized string.
	 */
	public String getString(String key) {
		Locale locale = getLocale();
		if (locale == null) {
			locale = Locale.UK;
		}
		return TextServer.getString(key, locale);
	}

	/**
	 * A <code>TextServer</code> services text resources. Text resources can be located in property files under a common
	 * directory root, in property files directly passed to the server, or in compressed files available through the
	 * class path, that contain a set of property files.
	 * 
	 * @author Miquel Sas
	 */
	private static class TextServer {

		/** List of base resources loaded. */
		private static ArrayList<String> baseResources = new ArrayList<>();
		/** The text server that has the base resources loaded. */
		private static TextServer baseTextServer = new TextServer();
		/** A map with localized text servers. */
		private static HashMap<Locale, TextServer> localizedTextServers = new HashMap<>();

		/**
		 * Returns the not found string for a given key.
		 * 
		 * @param key The search key.
		 * @return The not found string.
		 */
		public static String notFoundKey(String key) {
			return "[" + key + "]";
		}

		/**
		 * Returns a string for a given locale.
		 * 
		 * @param key The key to search the string.
		 * @param locale The locale to use. base server when the key is not found.
		 * @return the String.
		 */
		private static String getString(String key, Locale locale) {
			TextServer textServer = null;
			String string = null;
			// Try with the appropriate server.
			textServer = getLocalizedTextServer(locale);
			string = getString(key, textServer, locale);
			if (string != null) {
				return string;
			}
			// Try with the base text server.
			textServer = getBaseTextServer();
			string = getString(key, textServer, new Locale(""));
			if (string != null) {
				return string;
			}
			// Then return the key.
			return notFoundKey(key);
		}

		/**
		 * Get a string from a given text server.
		 * 
		 * @param key The string key
		 * @param textServer The text server to use.
		 * @param locale The local use.
		 * @return The required string or null.
		 */
		private static String getString(String key, TextServer textServer, Locale locale) {
			String string = null;
			// First attempt to check if the string is already loaded in the serve.
			string = textServer.getServerString(key);
			if (string != null) {
				return string;
			}
			// A second attempt to load not loaded resources.
			for (int i = 0; i < baseResources.size(); i++) {
				String fileName = baseResources.get(i);
				if (textServer.hasLoaded(fileName)) {
					continue;
				}
				try {
					textServer.loadResource(fileName, locale);
				} catch (IOException e) {
					Logger.getLogger(TextServer.class).error(
						"Can't load the resource: " + fileName + " locale: " + locale);
				}
				string = textServer.getServerString(key);
				if (string != null) {
					return string;
				}
			}
			// Finally the string was not found.
			return null;
		}

		/**
		 * Adds a base resource to the list of base resources.
		 * 
		 * @param fileName The base resource to add.
		 */
		private static void addBaseResource(String fileName) {
			if (!baseResources.contains(fileName)) {
				baseResources.add(fileName);
			}
		}

		/**
		 * Returns the base text server.
		 * 
		 * @return The base text server.
		 */
		private static TextServer getBaseTextServer() {
			return baseTextServer;
		}

		/**
		 * Returns the localized text server.
		 * 
		 * @param locale The locale to use.
		 * @return The localized text server.
		 */
		private static TextServer getLocalizedTextServer(Locale locale) {
			TextServer textServer = localizedTextServers.get(locale);
			if (textServer == null) {
				textServer = new TextServer();
				localizedTextServers.put(locale, textServer);
			}
			return textServer;
		}

		/**
		 * The loaded properties.
		 */
		private Properties textProperties = new Properties();
		/**
		 * List of resources loaded in this server.
		 */
		private List<String> resources = new ArrayList<>();

		/**
		 * Default constructor.
		 */
		private TextServer() {
			super();
		}

		/**
		 * Check if the server has loaded the given resource.
		 * 
		 * @param fileName The file name of the text resource.
		 * @return A boolean.
		 */
		private boolean hasLoaded(String fileName) {
			return resources.contains(fileName);
		}

		/**
		 * Gets a string searching by key.
		 * 
		 * @param key The key to search.
		 * @return The string.
		 */
		private String getServerString(String key) {
			return textProperties.getProperty(key);
		}

		/**
		 * Load a resource file, either a normal properties file, or a zipped file with many properties files.
		 * 
		 * @param fileName The absolute file name.
		 * @param locale The locale or null to load base resources.
		 * @throws IOException If an IO error occurs.
		 */
		private void loadResource(String fileName, Locale locale) throws IOException {
			// Check the resource to load
			if (hasLoaded(fileName)) {
				return;
			}
			// Separate name and extension.
			String ext = Files.getFileExtension(fileName);
			// Check compressed.
			boolean zipped = (ext.equalsIgnoreCase("zip") || ext.equalsIgnoreCase("jar"));
			if (!zipped) {
				loadResourceStd(fileName, locale);
			} else {
				loadResourceZip(fileName, locale);
			}
			resources.add(fileName);
		}

		/**
		 * Load a normal resource file.
		 * 
		 * @param fileName The absolute file name.
		 * @param locale The locale or null to load base resources.
		 * @throws IOException If an IO error occurs.
		 */
		private void loadResourceStd(String fileName, Locale locale) throws IOException {
			String name = Files.getFileName(fileName);
			String ext = Files.getFileExtension(fileName);
			File file = Files.getLocalizedFile(locale, name, ext);
			if (file != null) {
				Properties properties = SystemUtils.getProperties(file);
				mergeResources(properties);
			}
		}

		/**
		 * Load the resources from the zip file, taking only those that apply the locale.
		 * 
		 * @param fileName The absolute file name.
		 * @param locale The locale or null to load base resources.
		 * @throws IOException If an IO error occurs.
		 */
		private void loadResourceZip(String fileName, Locale locale) throws IOException {
			File file = Files.getFileFromClassPathEntries(fileName);
			FileInputStream fis = new FileInputStream(file);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				String name = entry.getName();
				boolean merge = false;
				if (locale != null && isLocalizedResource(name, locale)) {
					merge = true;
				}
				if (locale == null && isBaseResource(name)) {
					merge = true;
				}
				if (merge) {
					mergeResources(SystemUtils.getProperties(zis));
				}
			}
			zis.close();
			fis.close();
		}

		/**
		 * Check if a resource name is a base name.
		 * 
		 * @param resourceName The resource name.
		 * @return A boolean that indicates if the resoource is a base resource.
		 */
		private boolean isBaseResource(String resourceName) {
			String name = Files.getFileName(resourceName);
			if (name.charAt(name.length() - 3) == '_') {
				return false;
			}
			return true;
		}

		/**
		 * Check if a resource is localized.
		 * 
		 * @param resourceName The resource name.
		 * @param locale The locale.
		 * @return A boolean that indicates if the resoource is a base resource.
		 */
		private boolean isLocalizedResource(String resourceName, Locale locale) {
			String name = Files.getFileName(resourceName);
			String language_country = locale.getLanguage() + "_" + locale.getCountry();
			String language = locale.getLanguage();
			if (name.endsWith(language_country) || name.endsWith(language)) {
				return true;
			}
			return false;
		}

		/**
		 * Merge the incoming properties with this text server properties.
		 * 
		 * @param properties The properties to merge with this text server.
		 */
		private void mergeResources(Properties properties) {
			Enumeration<Object> e = properties.keys();
			while (e.hasMoreElements()) {
				Object key = e.nextElement();
				if (!textProperties.containsKey(key)) {
					textProperties.put(key, properties.get(key));
				}
			}
		}
	}
}
