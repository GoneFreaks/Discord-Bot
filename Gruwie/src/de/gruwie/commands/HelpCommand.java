package de.gruwie.commands;

import de.gruwie.CommandManager;
import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand extends ServerCommand {
	
	public HelpCommand() {
		super(false, true, HelpCommand.class, "Help/Manual-Pages", "A collection of all commands available\nAlso the command to get help for other commands");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String[] args = message.getContentRaw().split(" ");
		CommandManager cmdMan = Gruwie_Startup.INSTANCE.getCmdMan();
		long guildId = channel.getGuild().getIdLong();
		
		if(args.length == 1) MessageManager.sendEmbedMessage(false, cmdMan.toString(), guildId, "Executed by: Java " + System.getProperty("java.version") + "\nVersion: " + Gruwie_Startup.VERSION);
		else {
			String command_symbol = ConfigManager.getString("symbol");
			
			String cmd = args[1].trim().replaceAll(command_symbol, "");
			ServerCommand scmd = cmdMan.getServerCommand(cmd);
			if(scmd != null) {
				
				StringBuilder b = new StringBuilder("");
				String desc = scmd.getDescription();
				b.append(scmd + "\n" + (desc != null? desc : "NA"));
				if(scmd.getSymbol() != null) b.append("\nThis command can also be used by pressing " + scmd.getSymbol() + " below the music-queue message");
				
				MessageManager.sendEmbedMessage(false, b.toString(), guildId, null);
			}
			else MessageManager.sendEmbedMessage(true, "**YOU HAVE PROVIDED AN UNKNOWN COMMAND**", channel, null);
		}
	}
}
