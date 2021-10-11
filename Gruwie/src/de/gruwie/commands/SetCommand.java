package de.gruwie.commands;

import de.gruwie.ConfigManager;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.ChannelManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetCommand implements ServerCommand {

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(member.hasPermission(Permission.MANAGE_CHANNEL)) {
			ChannelManager.putChannel(member.getGuild().getIdLong(), channel.getIdLong());
			String output1 = "**OUTPUT-CHANNEL HAS BEEN SET**";
			String output2 = ConfigManager.getBoolean("database")? "" : " TEMPORARILY";
			MessageManager.sendEmbedMessage(output1 + output2, ChannelManager.getChannel(channel), true);
		}
		else {
			MessageManager.sendEmbedMessage("**YOU DON'T HAVE THE PERMISSION TO YOU USE THIS COMMAND**", ChannelManager.getChannel(channel), true);
		}
	}

}
