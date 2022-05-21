package de.gruwie.commands.types;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.Outputs;
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
	private final String parameters;
	private final String optional_parameters;
	
	public ServerCommand(boolean beta, boolean tryShortcut, Class<?> callingClass, String symbol, int position, Outputs outputs_parameters, Outputs outputs_optional_parameters, Outputs outputs_short_description, Outputs outputs_description) {
		CommandDTO dto = Formatter.createNames(callingClass.getSimpleName(), tryShortcut);
		this.beta = beta;
		this.command = dto.getCommand();
		this.shortcut = dto.getShortcut();
		this.symbol = symbol;
		this.position = position;
		this.short_description = outputs_short_description != null? outputs_short_description.getValue() : null;
		this.description = outputs_description != null? outputs_description.getValue() : null;
		this.package_name = callingClass.getPackage().getName();
		this.parameters = outputs_parameters != null? outputs_parameters.getValue() : null;
		this.optional_parameters = outputs_optional_parameters != null? outputs_optional_parameters.getValue() : null;
	}
	
	public ServerCommand(boolean beta, boolean tryShortcut, Class<?> callingClass, Outputs outputs_parameters, Outputs outputs_optional_parameters, Outputs outputs_short_description, Outputs outputs_description) {
		this(beta, tryShortcut, callingClass, null, -1, outputs_parameters, outputs_optional_parameters, outputs_short_description, outputs_description);
	}
	
	public ServerCommand(boolean beta, boolean tryShortcut, Class<?> callingClass, Outputs outputs_short_description, Outputs outputs_description) {
		this(beta, tryShortcut, callingClass, null, -1, null, null, outputs_short_description, outputs_description);
	}
	
	public ServerCommand(boolean beta, boolean tryShortcut, Class<?> callingClass, String symbol, int position, Outputs outputs_short_description, Outputs outputs_description) {
		this(beta, tryShortcut, callingClass, symbol, position, null, null, outputs_short_description, outputs_description);
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
		return parameters;
	}
	
	public final String getOptionalParamters() {
		return optional_parameters;
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
		MessageManager.sendEmbedMessage(true, Outputs.TO_BE_DONE, channel);
		System.err.println(command);
	}

	public final int compareTo(ServerCommand other) {
		if(this.package_name.equals(other.getPackageName())) return this.getCommand().compareTo(other.getCommand());
		else return this.getPackageName().compareTo(other.getPackageName());
	}
	
}
