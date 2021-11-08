package de.gruwie.commands.types;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.dto.CommandDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandInfo implements ServerCommand{

	private String command;
	private String shortcut;
	private String symbol;
	private String short_description;
	private String description;
	private boolean wip;
	private String package_name;
	
	public CommandInfo(boolean wip, boolean tryShortcut, Class<?> callingClass, String symbol, String short_description, String description) {
		CommandDTO dto = Formatter.createNames(callingClass.getSimpleName(), tryShortcut);
		this.wip = wip;
		this.command = dto.getCommand();
		this.shortcut = dto.getShortcut();
		this.symbol = symbol;
		this.short_description = short_description;
		this.description = description;
		this.package_name = callingClass.getPackage().getName();
	}
	
	public CommandInfo(Class<?> callingClass) {
		this(true, false, callingClass, null, null, null);
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public String getShortcut() {
		return shortcut;
	}

	@Override
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public boolean isWip() {
		return wip;
	}
	
	@Override
	public String getPackageName() {
		return package_name;
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

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		System.out.println("NOT YET IMPLEMENTED");
	}

	@Override
	public int compareTo(ServerCommand other) {
		if(this.package_name.equals(other.getPackageName())) return this.getCommand().compareTo(other.getCommand());
		else return this.getPackageName().compareTo(other.getPackageName());
	}
	
}
