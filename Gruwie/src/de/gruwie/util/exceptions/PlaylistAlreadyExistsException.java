package de.gruwie.util.exceptions;

@SuppressWarnings("serial")
public class PlaylistAlreadyExistsException extends Exception {

	public PlaylistAlreadyExistsException (String msg) {
		super(msg);
	}
	
}
