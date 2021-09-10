package de.heiko.command;

import de.heiko.command.types.ServerCommand;
import de.heiko.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class RepeatCommand implements ServerCommand{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message) {
		MessageManager.sendMessage(message.getContentDisplay(), channel, true);
	}

}
