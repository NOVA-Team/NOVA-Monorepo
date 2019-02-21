/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */package nova.core.util;

import nova.core.util.exception.NovaException;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author rx14
 */
public class ClassLoaderUtil {
	private static Method addURL;

	public static void addURLToClassPath(URL url) {
		if (addURL != null) setAddURL();

		try {
			addURL.invoke(ClassLoader.getSystemClassLoader(), url);
		} catch (Throwable t) {
			throw new ClassLoaderException("Failed to add " + url + " to the classpath");
		}
	}

	public static void addFileToClassPath(File file) {
		try {
			addURLToClassPath(file.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new ClassLoaderException("Failed to convert " + file.getAbsolutePath() + " to URL");
		}
	}

	private static synchronized void setAddURL() {
		try {
			Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			m.setAccessible(true);
			addURL = m;
		} catch (NoSuchMethodException e) {
			throw new ClassLoaderException("URLClassLoader has no addURL method. All of my wat.", e);
		}
	}

	public static class ClassLoaderException extends NovaException {
		private static final long serialVersionUID = 1L;

		public ClassLoaderException() {
			super();
		}

		public ClassLoaderException(String message, Object... parameters) {
			super(message, parameters);
		}

		public ClassLoaderException(String message) {
			super(message);
		}

		public ClassLoaderException(String message, Throwable cause) {
			super(message, cause);
		}

		public ClassLoaderException(Throwable cause) {
			super(cause);
		}
	}
}
