package de.gruwie.commands;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.jda.MessageManager;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ClearCommand extends ServerCommand {
	
	public ClearCommand() {
		super(false, true, ClearCommand.class, null, "**n** the number of messages to delete", "Delete messages", "Delete messages in the current channel\nYou need the permission to manage messages in order to use this command");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(member.hasPermission(Permission.MESSAGE_MANAGE)) {
			String[] args = message.getContentRaw().split(" ");
			if(args.length == 2) {
				try {
					int delete = Integer.parseInt(args[1]);
					if(delete < 10) deleteMessages(delete, channel, message);
					else deleteBulkMessages(delete, channel, message);
				} catch (Exception e) {
					MessageManager.sendEmbedMessage(true, "**YOU HAVE TO PROVIDE A NUMBER**", channel, null);
				}
			}
			if(args.length == 1) deleteMessages(1, channel, message);
			if(args.length != 1 && args.length != 2) MessageManager.sendEmbedMessage(true, "**WRONG PATTERN, YOU HAVE TO PROVIDE EITHER ONE ARGUMENT OR NONE**", channel, null);
		}
		else MessageManager.sendEmbedMessage(true, "**YOU DONT HAVE THE PERMISSION TO USE THIS COMMAND**", channel, null);
	}
	
	private void deleteBulkMessages(int delete, TextChannel channel, Message message) {
		channel.getHistoryAfter(message, delete).queue((history) -> {
			history.retrievePast(delete).queue((messages) -> {
				List<List<Message>> list = new ArrayList<>();
				List<Message> list_messages = new ArrayList<>(100);
				for(Message i: messages) {
					list_messages.add(i);
					if(list_messages.size() >= 100) {
						list.add(list_messages);
						list_messages = new ArrayList<>(100);
					}
					if(list_messages.size() > 0) list.add(list_messages);
				}
				try {
					for (List<Message> i : list) {
						channel.deleteMessages(i).queue();
					}
				} catch (Exception e) {
					deleteMessages(delete, channel, message);
				}
			});
		});
	}
	
	private void deleteMessages(int delete, TextChannel channel, Message message) {
		channel.getHistoryAfter(message, delete).queue((history) ->{
			history.retrievePast(delete).queue((messages) ->{
				for (Message i : messages) {
					try {
						i.delete().queue(null, Filter.handler);
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		});
	}

}
