package de.gruwie.commands;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ClearCommand extends CommandInfo implements ServerCommand {
	
	public ClearCommand() {
		super(false, true, ClearCommand.class.getSimpleName(), null, "Delete the given amount of messages in the current channel\nYou need the permission to manage messages in order to use this command");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(member.hasPermission(Permission.MESSAGE_MANAGE)) {
			String[] args = message.getContentRaw().split(" ");
			if(args.length == 2) {
				int delete = Integer.parseInt(args[1]);
				channel.getHistoryAfter(message, delete).queue((history) -> {
					history.retrievePast(delete).queue((messages) -> {
						for(Message i: messages) i.delete().queue(null, ErrorClass.getErrorHandler());
					});
				});
			}
			else MessageManager.sendEmbedMessage("**WRONG PATTERN, YOU HAVE TO PROVIDE EXACTLY ONE ARGUMENT**", channel, true);
		}
		else MessageManager.sendEmbedMessage("**YOU DONT HAVE THE PERMISSION TO USE THIS COMMAND**", channel, true);
	}

}
