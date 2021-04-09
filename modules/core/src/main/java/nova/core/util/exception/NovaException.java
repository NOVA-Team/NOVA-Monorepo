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
 */

package nova.core.util.exception;

/**
 * General exception that can be thrown by Nova internals
 */
public abstract class NovaException extends RuntimeException {
	private static final long serialVersionUID = -2692979724920608046L;

	/**
	 *
	 */
	public NovaException() {
		super();
	}

	/**
	 * Formatted with {@link String#format(String, Object...)}
	 *
	 * @param message The error message
	 * @param parameters additional parameters
	 */
	public NovaException(String message, Object... parameters) {
		this(String.format(message, parameters));
	}

	/**
	 * General exception that can be thrown by Nova internals
	 *
	 * @param message Error message
	 */
	public NovaException(String message) {
		super(message);
	}

	/**
	 * General exception that can be thrown by Nova internals
	 *
	 * @param message Error message
	 * @param cause Cause of the exception
	 */
	public NovaException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * General exception that can be thrown by Nova internals
	 *
	 * @param cause Cause of the exception
	 */
	public NovaException(Throwable cause) {
		super(cause);
	}
}
