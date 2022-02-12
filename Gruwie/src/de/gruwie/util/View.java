package de.gruwie.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.dto.SymbolDTO;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class View {

	private static List<SymbolDTO> emotes = new ArrayList<>();
	
	private Message current_track_view;
	private Message current_queue_view;
	
	public View(Message track_view, MessageEmbed embed) {
		MessageAction action = track_view.getChannel().sendMessageEmbeds(embed);
		
		List<Button> buttons = new ArrayList<>();
		for (int i = 0; i < emotes.size(); i++) {
			SymbolDTO k = emotes.get(i);
			if(i == 0) buttons.add(Button.danger(k.getCmd(), Emoji.fromMarkdown(k.getSymbol())));
			else buttons.add(Button.success(k.getCmd(), Emoji.fromMarkdown(k.getSymbol())));
		}
		
		action.setActionRow(buttons);
		this.current_queue_view = action.complete();
		this.current_track_view = track_view;
	}
	
	public void deleteView() {
		if(current_track_view != null) current_track_view.delete().queue(null, Filter.handler);
		if(current_queue_view != null) current_queue_view.delete().queue(null, Filter.handler);
		current_track_view = null;
		current_queue_view = null;
	}
	
	public Message getQueueView () {
		return current_queue_view;
	}
	
	public static void init(List<ServerCommand> commands) {
		for (ServerCommand i : commands) {
			if(i.getSymbol() != null) emotes.add(new SymbolDTO(i.getSymbol(), i.getPosition(), i.getCommand()));
		}
		Collections.sort(emotes);
	}
}
