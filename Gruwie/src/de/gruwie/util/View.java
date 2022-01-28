package de.gruwie.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.helper.ProgressBar;
import de.gruwie.util.dto.Symbol;
import net.dv8tion.jda.api.entities.Message;

public class View {

	private static List<Symbol> emotes = new ArrayList<>();
	
	private Message current_track_view;
	private Message current_queue_view;
	private ProgressBar current_progress_bar;
	private Thread current_progress_bar_thread;
	
	public View(Message track_view, Message queue_view, ProgressBar current_progress_bar) {
		
		this.current_track_view = track_view;
		this.current_queue_view = queue_view;
		this.current_progress_bar = current_progress_bar;
		
		if(current_progress_bar != null) {
			this.current_progress_bar_thread = new Thread(current_progress_bar);
			this.current_progress_bar_thread.setDaemon(true);
			this.current_progress_bar_thread.start();
		}
		else current_progress_bar_thread = null;
		
		for (int i = 0; i < emotes.size(); i++) {
			current_queue_view.addReaction(emotes.get(i).getSymbol()).queue(null, Filter.handler);
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
	
	public static void init(List<ServerCommand> commands) {
		for (ServerCommand i : commands) {
			if(i.getSymbol() != null) emotes.add(new Symbol(i.getSymbol(), i.getPosition()));
		}
		Collections.sort(emotes);
	}
}
