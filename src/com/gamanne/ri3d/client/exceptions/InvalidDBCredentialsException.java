package com.gamanne.ri3d.client.exceptions;
/**
 * Throws when there is a problem with database credentials
 * @author Joaquin Fernandez Moreno
 *
 */
@SuppressWarnings("serial")
public class InvalidDBCredentialsException extends Exception {
	public InvalidDBCredentialsException(String string) {
		super(string);
	}
}
