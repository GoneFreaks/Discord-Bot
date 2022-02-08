package de.gruwie.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.dto.SymbolDTO;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.Message;

public class View {

	private static List<SymbolDTO> emotes = new ArrayList<>();
	
	private Message current_track_view;
	private Message current_queue_view;
	
	public View(Message track_view, Message queue_view) {
		
		this.current_track_view = track_view;
		this.current_queue_view = queue_view;
		
		for (int i = 0; i < emotes.size(); i++) {
			current_queue_view.addReaction(emotes.get(i).getSymbol()).queue(null, Filter.handler);
		}
	}
	
	public void deleteView() {
		if(current_track_view != null) current_track_view.delete().queue();
		if(current_queue_view != null) current_queue_view.delete().queue();
		current_track_view = null;
		current_queue_view = null;
	}
	
	public Message getQueueView () {
		return current_queue_view;
	}
	
	public static void init(List<ServerCommand> commands) {
		for (ServerCommand i : commands) {
			if(i.getSymbol() != null) emotes.add(new SymbolDTO(i.getSymbol(), i.getPosition()));
		}
		Collections.sort(emotes);
	}
}
