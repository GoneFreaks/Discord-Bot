package de.gruwie.commands.types;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.CommandDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ServerCommand implements Comparable<ServerCommand>{

	private final String command;
	private final String shortcut;
	private final String symbol;
	private final int position;
	private final String short_description;
	private final String description;
	private final boolean wip;
	private final String package_name;
	
	public ServerCommand(boolean wip, boolean tryShortcut, Class<?> callingClass, String symbol, int position, String short_description, String description) {
		CommandDTO dto = Formatter.createNames(callingClass.getSimpleName(), tryShortcut);
		this.wip = wip;
		this.command = dto.getCommand();
		this.shortcut = dto.getShortcut();
		this.symbol = symbol;
		this.position = position;
		this.short_description = short_description;
		this.description = description;
		this.package_name = callingClass.getPackage().getName();
	}
	
	public ServerCommand(boolean wip, boolean tryShortcut, Class<?> callingClass, String short_description, String description) {
		this(wip, tryShortcut, callingClass, null, -1, short_description, description);
	}
	
	public ServerCommand(Class<?> callingClass) {
		this(true, false, callingClass, null, -1, null, null);
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
	
	public int getPosition() {
		return position;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean isWip() {
		return wip;
	}
	
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

	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MessageManager.sendEmbedMessage(true, "**NOT YET IMPLEMENTED**", channel, null);
		System.err.println(command);
	}

	public int compareTo(ServerCommand other) {
		if(this.package_name.equals(other.getPackageName())) return this.getCommand().compareTo(other.getCommand());
		else return this.getPackageName().compareTo(other.getPackageName());
	}
	
}
