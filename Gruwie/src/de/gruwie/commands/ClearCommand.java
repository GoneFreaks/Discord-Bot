package de.gruwie.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ClearCommand extends ServerCommand {
	
	public ClearCommand() {
		super(false, true, ClearCommand.class, null, "Delete messages", "Delete the given amount of messages in the current channel\nYou need the permission to manage messages in order to use this command");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(member.hasPermission(Permission.MESSAGE_MANAGE)) {
			String[] args = message.getContentRaw().split(" ");
			if(args.length == 2) deleteMessages(Integer.parseInt(args[1]), channel, message);
			if(args.length == 1) deleteMessages(1, channel, message);
			if(args.length != 1 && args.length != 2) MessageManager.sendEmbedMessage("**WRONG PATTERN, YOU HAVE TO PROVIDE EITHER ONE ARGUMENT OR NONE**", channel);
		}
		else MessageManager.sendEmbedMessage("**YOU DONT HAVE THE PERMISSION TO USE THIS COMMAND**", channel);
	}
	
	private void deleteMessages(int delete, TextChannel channel, Message message) {
		channel.getHistoryAfter(message, delete).queue((history) -> {
			history.retrievePast(delete).queue((messages) -> {
				for(Message i: messages) i.delete().queue(null, ErrorClass.getErrorHandler());
			});
		});
	}

}
