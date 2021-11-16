package de.gruwie.util.dto;

import de.gruwie.music.helper.ProgressBar;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;

public class ViewDTO {

	private final static String[] EMOTES = {"⏹️", "⏯️", "⏩", "⏭️", "🔁", "🔀", "🆕", "🔊", "🔉"};
	
	private Message current_track_view;
	private Message current_queue_view;
	private ProgressBar current_progress_bar;
	private Thread current_progress_bar_thread;
	
	public ViewDTO(Message track_view, Message queue_view, ProgressBar current_progress_bar) {
		
		this.current_track_view = track_view;
		this.current_queue_view = queue_view;
		this.current_progress_bar = current_progress_bar;
		
		if(current_progress_bar != null) {
			this.current_progress_bar_thread = new Thread(current_progress_bar);
			this.current_progress_bar_thread.start();
		}
		else current_progress_bar_thread = null;
		
		for (int i = 0; i < EMOTES.length; i++) {
			current_queue_view.addReaction(EMOTES[i]).queue(null, ErrorClass.getErrorHandler());
		}
	}
	
	public void deleteView() {
		if(current_track_view != null) current_track_view.delete().queue();
		if(current_queue_view != null) current_queue_view.delete().queue();
		if(current_progress_bar != null) current_progress_bar_thread.interrupt();
		current_track_view = null;
		current_queue_view = null;
		current_progress_bar = null;
		current_progress_bar = null;
	}
	
	public void editCurrentQueueView(String new_message) {
		if(current_queue_view != null) {
			if(current_progress_bar_thread == null) MessageManager.editMessage(current_queue_view, new_message);
			else current_progress_bar.editMessage();
		}
	}
}
