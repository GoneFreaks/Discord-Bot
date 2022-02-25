package de.gruwie.commands.types;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.dto.CommandDTO;
import de.gruwie.util.jda.MessageManager;
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
	private final boolean beta;
	private final String package_name;
	private final String paramters;
	private final String optional_paramters;
	
	public ServerCommand(boolean beta, boolean tryShortcut, Class<?> callingClass, String symbol, int position, String paramters, String optional_paramters, String short_description, String description) {
		CommandDTO dto = Formatter.createNames(callingClass.getSimpleName(), tryShortcut);
		this.beta = beta;
		this.command = dto.getCommand();
		this.shortcut = dto.getShortcut();
		this.symbol = symbol;
		this.position = position;
		this.short_description = short_description;
		this.description = description;
		this.package_name = callingClass.getPackage().getName();
		this.paramters = paramters;
		this.optional_paramters = optional_paramters;
	}
	
	public ServerCommand(boolean beta, boolean tryShortcut, Class<?> callingClass, String paramters, String optional_paramters, String short_description, String description) {
		this(beta, tryShortcut, callingClass, null, -1, paramters, optional_paramters, short_description, description);
	}
	
	public ServerCommand(boolean beta, boolean tryShortcut, Class<?> callingClass, String short_description, String description) {
		this(beta, tryShortcut, callingClass, null, -1, null, null, short_description, description);
	}
	
	public ServerCommand(boolean beta, boolean tryShortcut, Class<?> callingClass, String symbol, int position, String short_description, String description) {
		this(beta, tryShortcut, callingClass, symbol, position, null, null, short_description, description);
	}
	
	public ServerCommand(Class<?> callingClass) {
		this(true, false, callingClass, null, -1, null, null, null, null);
	}

	public final String getCommand() {
		return command;
	}

	public final String getShortcut() {
		return shortcut;
	}

	public final String getSymbol() {
		return symbol;
	}
	
	public final int getPosition() {
		return position;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public final boolean isBeta() {
		return beta;
	}
	
	public final String getPackageName() {
		return package_name;
	}
	
	public final String getParamters() {
		return paramters;
	}
	
	public final String getOptionalParamters() {
		return optional_paramters;
	}
	
	@Override
	public final String toString() {
		String cmd_symbol = ConfigManager.getString("symbol");
		StringBuilder b = new StringBuilder(beta? "**BETA**--------------------------------------------------------\n" : "");
		b.append("Command: *" + cmd_symbol + command + "*\n");
		if(shortcut != null) b.append("Shortcut: *" + cmd_symbol + shortcut + "*\n");
		
		if(symbol != null) b.append("Symbol: *" + symbol + "*\n");
		if(short_description != null) b.append("Description: *" + short_description + "*\n");
		return b.toString() + (beta? "---------------------------------------------------------------\n" : "");
	}

	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MessageManager.sendEmbedMessage(true, "**NOT YET IMPLEMENTED**", channel, null);
		System.err.println(command);
	}

	public final int compareTo(ServerCommand other) {
		if(this.package_name.equals(other.getPackageName())) return this.getCommand().compareTo(other.getCommand());
		else return this.getPackageName().compareTo(other.getPackageName());
	}
	
}
