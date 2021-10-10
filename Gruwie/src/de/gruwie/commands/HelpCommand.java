package de.gruwie.commands;

import de.gruwie.CommandManager;
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
		
		CommandManager cmdMan = Gruwie_Startup.INSTANCE.getCmdMan();
		
		MessageManager.sendEmbedMessage(cmdMan.toString(), ChannelManager.getChannel(channel), false);
		
	}

}
