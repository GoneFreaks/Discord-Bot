package de.gruwie.util.exceptions;

@SuppressWarnings("serial")
public class IdentifierAlreadyTakenException extends Exception {

	public IdentifierAlreadyTakenException (String msg) {
		super(msg);
	}
	
}
