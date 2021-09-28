package de.gruwie.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.DataManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetCommand implements ServerCommand {

	@Override
	public void performCommand(Member member, TextChannel channel, Message message) {
		DataManager.putChannel(member.getGuild().getIdLong(), channel.getIdLong());
	}

}
