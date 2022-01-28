package de.gruwie.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.ChannelManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetCommand extends ServerCommand {
	
	public SetCommand() {
		super(false, false, SetCommand.class, "Set Output-Channel" ,"Choose which channel Gruwie should use as the output-channel\nGruwie will react to messages regardless of the channel they were sent in");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(member.hasPermission(Permission.MANAGE_CHANNEL)) {
			ChannelManager.putChannel(member.getGuild().getIdLong(), channel.getIdLong());
			MessageManager.sendEmbedMessage(true, "**OUTPUT-CHANNEL HAS BEEN SET**" + (ConfigManager.getDatabase()? "" : " **TEMPORARILY**"), channel, null);
		}
		else MessageManager.sendEmbedMessage(true, "**YOU DON'T HAVE THE PERMISSION TO YOU USE THIS COMMAND**", channel, null);
	}
	
}
