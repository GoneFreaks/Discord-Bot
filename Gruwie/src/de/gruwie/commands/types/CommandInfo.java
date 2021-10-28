package de.gruwie.commands.types;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.dto.CommandDTO;

public class CommandInfo{

	private String command;
	private String shortcut;
	private String symbol;
	private String short_description;
	private String description;
	private boolean wip;
	
	public CommandInfo(boolean wip, boolean tryShortcut, String classname, String symbol, String short_description, String description) {
		CommandDTO dto = Formatter.createNames(classname, tryShortcut);
		this.wip = wip;
		this.command = dto.getCommand();
		this.shortcut = dto.getShortcut();
		this.symbol = symbol;
		this.short_description = short_description;
		this.description = description;
	}
	
	public CommandInfo(String classname) {
		this(true, false, classname, null, null, null);
	}

	public String getCommand() {
		return command;
	}

	public String getShortcut() {
		return shortcut;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		String cmd_symbol = ConfigManager.getString("symbol");
		StringBuilder b = new StringBuilder(wip? "**WIP**------------------------------\n" : "");
		b.append("Command: *" + cmd_symbol + command + "*\n");
		if(shortcut != null) b.append("Shortcut: *" + cmd_symbol + shortcut + "*\n");
		if(symbol != null) b.append("Symbol: *" + symbol + "*\n");
		if(short_description != null) b.append("Description: *" + short_description + "*\n");
		return b.toString() + (wip? "-----------------------------------\n" : "");
	}
	
}
