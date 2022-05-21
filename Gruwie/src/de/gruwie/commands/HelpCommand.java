package de.gruwie.commands;

import de.gruwie.CommandManager;
import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Outputs;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand extends ServerCommand {
	
	public HelpCommand() {
		super(false, true, HelpCommand.class, null, Outputs.OPTIONAL_PARAMETERS_HELP, Outputs.SHORT_DESCRIPTION_HELP, Outputs.DESCRIPTION_HELP);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String[] args = message.getContentStripped().split(" ");
		CommandManager cmdMan = Gruwie_Startup.INSTANCE.getCmdMan();
		long guildId = channel.getGuild().getIdLong();
		
		if(args.length == 1) MessageManager.sendEmbedMessageVariable(false, cmdMan.toString(), guildId, Outputs.DEFAULT_FOOTER);
		else {
			String command_symbol = ConfigManager.getString("symbol");
			
			String cmd = args[1].trim().replaceAll(command_symbol, "");
			ServerCommand scmd = cmdMan.getServerCommand(cmd.toLowerCase());
			if(scmd != null) {
				
				StringBuilder b = new StringBuilder("");
				
				b.append(scmd);
				String parameters = scmd.getParamters();
				if(parameters != null) b.append("Mandatory Parameters: *" + parameters + "*\n");
				String optional_parameters = scmd.getOptionalParamters();
				if(optional_parameters != null) b.append("Optional Paramters: *" + optional_parameters + "*\n");
				String desc = scmd.getDescription();
				b.append(desc != null? "\n" + desc : "NA");
				if(scmd.getSymbol() != null) b.append("\nThis command can also be used by pressing " + scmd.getSymbol() + " below the music-queue message");
				
				
				MessageManager.sendEmbedMessageVariable(false, b.toString(), guildId, Outputs.DEFAULT_FOOTER);
			}
			else MessageManager.sendEmbedMessage(true, Outputs.INVALID_PARAMETERS, channel);
		}
	}
}
