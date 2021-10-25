package de.gruwie.commands.types;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.dto.CommandDTO;

public class CommandInfo{

	private String command;
	private String shortcut;
	private String symbol;
	private String description;
	private boolean wip;
	
	public CommandInfo(boolean wip, boolean tryShortcut, String classname, String symbol, String description) {
		CommandDTO dto = Formatter.createNames(classname, tryShortcut);
		this.wip = wip;
		this.command = dto.getCommand();
		this.shortcut = dto.getShortcut();
		this.symbol = symbol;
		this.description = description;
	}
	
	public CommandInfo(boolean wip, boolean tryShortcut, String classname) {
		this(wip, tryShortcut, classname, null, null);
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
		return b.toString() + (wip? "-----------------------------------\n" : "");
	}
	
}
