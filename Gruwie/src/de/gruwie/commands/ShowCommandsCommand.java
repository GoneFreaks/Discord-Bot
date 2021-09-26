package de.gruwie.commands;

import de.gruwie.CommandManager;
import de.gruwie.Gruwie_Startup;
import de.gruwie.command.types.ServerCommand;
import de.gruwie.db.DataManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShowCommandsCommand implements ServerCommand{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message) {
		TextChannel output_channel = DataManager.getChannel(channel.getGuild().getIdLong());
	
		CommandManager cmdMan = Gruwie_Startup.INSTANCE.getCmdMan();
		
		MessageManager.sendEmbedMessage(cmdMan.MapToString(), output_channel, false);
		
	}

}
