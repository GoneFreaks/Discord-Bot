package de.gruwie.util.dto;

public class CommandDTO {

	private String command;
	private String shortcut;
	
	public CommandDTO(String command, String shortcut) {
		this.command = command;
		this.shortcut = shortcut;
	}

	public String getCommand() {
		return command;
	}

	public String getShortcut() {
		return shortcut;
	}
	
}
