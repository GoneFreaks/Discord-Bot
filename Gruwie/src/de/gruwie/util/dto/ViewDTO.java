package de.gruwie.util.dto;

import de.gruwie.util.ErrorClass;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;

public class ViewDTO {

	private final static String[] EMOTES = {"⏹️", "⏯️", "⏩", "⏭️", "🔁", "🔀", "🆕"};
	
	private Message current_track_view;
	private Message current_queue_view;
	
	public ViewDTO(Message track_view, Message queue_view) {
		this.current_track_view = track_view;
		this.current_queue_view = queue_view;
		
		for (int i = 0; i < EMOTES.length; i++) {
			if(current_queue_view != null) current_queue_view.addReaction(EMOTES[i]).queue(null, ErrorClass.getErrorHandler());
		}
	}
	
	public void deleteView() throws Exception {
		if(current_track_view != null) current_track_view.delete().queue(null, ErrorClass.getErrorHandler());
		if(current_queue_view != null) current_queue_view.delete().queue(null, ErrorClass.getErrorHandler());
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
