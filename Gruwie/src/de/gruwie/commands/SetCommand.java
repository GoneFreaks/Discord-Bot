package de.gruwie.commands;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.ChannelManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetCommand extends CommandInfo implements ServerCommand {
	
	public SetCommand() {
		super(SetCommand.class.getSimpleName(), null, "Choose which channel Gruwie should use as the output-channel");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(member.hasPermission(Permission.MANAGE_CHANNEL)) {
			ChannelManager.putChannel(member.getGuild().getIdLong(), channel.getIdLong());
			MessageManager.sendEmbedMessage("**OUTPUT-CHANNEL HAS BEEN SET**" + (ConfigManager.getBoolean("database")? "" : " TEMPORARILY"), channel, true);
		}
		else MessageManager.sendEmbedMessage("**YOU DON'T HAVE THE PERMISSION TO YOU USE THIS COMMAND**", channel, true);
	}
	
}
