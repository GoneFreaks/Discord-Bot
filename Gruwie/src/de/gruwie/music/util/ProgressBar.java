package de.gruwie.music.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.music.Queue;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Message;

public class ProgressBar implements Runnable {

	private final Queue queue;
	private long last_position;
	
	public ProgressBar(Queue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(ConfigManager.getRefreshTimer() * 1000);
				editMessage(queue.getCurrentTrack());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void editMessage(AudioTrack current) {
		if(current != null) {
			long current_position = current.getPosition();
			if(last_position != current_position) {
				Message m = queue.getQueueView();
				if(m != null) MessageManager.editMessage(queue.getQueueView(), queue.toString() + listToString(current_position, current.getDuration()));
			}
			last_position = current_position;
		}
	}
	
	private boolean shown = false;
	private int counter = 0;
	private String listToString(long current_position, long duration) {
		StringBuilder b = new StringBuilder("\n");
		if(shown) b.append(":arrow_forward:"); 
		else b.append(":black_medium_square:");
		counter++;
		if(counter >= 2) {
			shown = !shown;
			counter = 0;
		}
		b.append(" " + Formatter.formatTime(current_position) + "/" + Formatter.formatTime(duration));
		b.append(" `" + checkProcess(current_position, duration) + "`");
		return b.toString();
	}
	
	private String checkProcess(long current_position, long duration) {
		
		StringBuilder b = new StringBuilder("");
		
		double percentage = ((current_position + 0.0) / duration);
		int progress = (int) Math.ceil((percentage * ConfigManager.getInteger("accuracy")));
		
		for (int i = 0; i < progress; i++) b.append("█");
		for (int i = 0; i < ConfigManager.getInteger("accuracy") - progress; i++) b.append(" ‎");
		
		return b.toString();
		
	}
}
