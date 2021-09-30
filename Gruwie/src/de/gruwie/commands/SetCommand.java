package de.gruwie.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.DataManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetCommand implements ServerCommand {

	@Override
	public void performCommand(Member member, TextChannel channel, Message message) {
		
		if(member.hasPermission(Permission.MANAGE_CHANNEL)) {
			DataManager.putChannel(member.getGuild().getIdLong(), channel.getIdLong());
			MessageManager.sendEmbedMessage("OUTPUT-CHANNEL HAS BEEN SET", DataManager.getChannel(channel), true);
		}
		else {
			MessageManager.sendEmbedMessage("YOU DON'T HAVE THE PERMISSION TO YOU USE THIS COMMAND", DataManager.getChannel(channel), true);
		}
	}

}
