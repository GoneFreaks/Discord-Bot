package de.gruwie.util.dto;

import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;

public class ViewDTO {

	private final static String[] EMOTES = {"⏹️", "⏯️", "⏩", "⏭️", "🔁", "🆕", "⏫", "⏬"};
	
	private Message current_track_view;
	private Message current_queue_view;
	
	public ViewDTO(Message track_view, Message queue_view) {
		this.current_track_view = track_view;
		this.current_queue_view = queue_view;
		
		for (int i = 0; i < EMOTES.length; i++) {
			if(current_queue_view != null) current_queue_view.addReaction(EMOTES[i]).queue();
		}
	}
	
	public void deleteView() {
		if(current_track_view != null) current_track_view.delete().complete();
		if(current_queue_view != null) current_queue_view.delete().complete();
		current_track_view = null;
		current_queue_view = null;
	}

	public Message getCurrentTrackView() {
		return current_track_view;
	}

	public Message getCurrentQueueView() {
		return current_queue_view;
	}
	
	public void editCurrentQueueView(String new_message) {
		if(current_queue_view != null) MessageManager.editMessage(current_queue_view, new_message);
	}
	
	
	
}
