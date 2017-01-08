/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util.id;

import nova.core.util.exception.NovaException;

/**
 * @author ExE Boss
 */
public class NoSuchIDException extends NovaException {
	private static final long serialVersionUID = 2017_01_08L;

	public NoSuchIDException() {
	}

	public NoSuchIDException(String message, Object... parameters) {
		super(message, parameters);
	}

	public NoSuchIDException(String message) {
		super(message);
	}

	public NoSuchIDException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchIDException(Throwable cause) {
		super(cause);
	}
}
