package de.gruwie.commands;

import de.gruwie.CommandManager;
import de.gruwie.ConfigManager;
import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.ChannelManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand implements ServerCommand{

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String[] args = message.getContentRaw().split(" ");
		CommandManager cmdMan = Gruwie_Startup.INSTANCE.getCmdMan();
		
		if(args.length == 1) MessageManager.sendEmbedMessage(cmdMan.toString(), ChannelManager.getChannel(channel), false);
		else {
			
			String command_symbol = ConfigManager.getString("symbol");
			
			String cmd = args[1].trim().replaceAll(command_symbol, "");
			ServerCommand scmd = cmdMan.getServerCommand(cmd);
			if(scmd != null) {
				
				StringBuilder b = new StringBuilder("");
				b.append("**Command: " + command_symbol + scmd.getCommand() + "**\n");
				if(scmd.getShortcut() != null) b.append("**Shortcut: " + command_symbol + scmd.getShortcut() + "**\n");
				if(scmd.getSymbol() != null) b.append("**Symbol: " + scmd.getSymbol() + "**\n");
				b.append("\n" + scmd.getDescription());
				if(scmd.getSymbol() != null) b.append("\nThis command can also be used by pressing " + scmd.getSymbol() + " below the music-queue message");
				
				MessageManager.sendEmbedMessage(b.toString(), ChannelManager.getChannel(channel), false);
			}
			else MessageManager.sendEmbedMessage("**YOU HAVE PROVIDED AN UNKNOWN COMMAND**", ChannelManager.getChannel(channel), true);
		}
	}
	
	@Override
	public String getDescription() {
		return "A collection of all commands available\nAlso the command to get help for other commands";
	}

	@Override
	public String getCommand() {
		return "help";
	}

	@Override
	public String getShortcut() {
		return "h";
	}

	@Override
	public String getSymbol() {
		return null;
	}

}
