package de.gruwie.commands;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.MessageManager;
import de.gruwie.util.Outputs;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ClearCommand extends ServerCommand {
	
	public ClearCommand() {
		super(false, true, ClearCommand.class, null, Outputs.OPTIONAL_PARAMETERS_CLEAR, Outputs.SHORT_DESCRIPTION_CLEAR, Outputs.DESCRIPTION_CLEAR);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(member.hasPermission(Permission.MESSAGE_MANAGE)) {
			String[] args = message.getContentStripped().split(" ");
			if(args.length == 2) {
				try {
					int delete = Integer.parseInt(args[1]);
					if(delete < 10) deleteMessages(delete, channel, message);
					else deleteBulkMessages(delete, channel, message);
				} catch (Exception e) {
					MessageManager.sendEmbedMessage(true, Outputs.INVALID_PARAMETERS, channel);
				}
			}
			if(args.length == 1) deleteMessages(1, channel, message);
			if(args.length != 1 && args.length != 2) MessageManager.sendEmbedMessage(true, Outputs.INVALID_PARAMETERS, channel);
		}
		else MessageManager.sendEmbedMessage(true, Outputs.PERMISSION, channel);
	}
	
	private void deleteBulkMessages(int delete, TextChannel channel, Message message) {
		channel.getHistoryAfter(message, delete).queue((history) -> {
			history.retrievePast(delete).queue((messages) -> {
				List<List<Message>> list = new ArrayList<>();
				List<Message> list_messages = new ArrayList<>(100);
				for(Message i: messages) {
					if(i.isPinned()) continue;
					list_messages.add(i);
					if(list_messages.size() >= 100) {
						list.add(list_messages);
						list_messages = new ArrayList<>(100);
					}
					if(list_messages.size() > 0) list.add(list_messages);
				}
				try {
					for (List<Message> i : list) {
						channel.deleteMessages(i).queue(null, Filter.handler);
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
					if(i.isPinned()) continue;
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
