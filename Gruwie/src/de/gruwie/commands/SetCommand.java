package de.gruwie.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.ChannelManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import de.gruwie.util.Outputs;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetCommand extends ServerCommand {
	
	public SetCommand() {
		super(false, false, SetCommand.class, Outputs.SHORT_DESCRIPTION_SET, Outputs.DESCRIPTION_SET);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		GruwieUtilities.log();
		if(member.hasPermission(Permission.MANAGE_CHANNEL)) {
			TextChannel old_channel = ChannelManager.getChannel(member.getGuild().getIdLong());
			GruwieUtilities.log("Output-Channel: " + old_channel.getName() + " --> " + channel.getName());
			ChannelManager.putChannel(member.getGuild().getIdLong(), channel.getIdLong());
			MessageManager.sendEmbedMessage(true, Outputs.OUTPUT_CHANNEL_SET, channel);
		}
		else MessageManager.sendEmbedMessage(true, Outputs.PERMISSION, channel);
	}
	
}
