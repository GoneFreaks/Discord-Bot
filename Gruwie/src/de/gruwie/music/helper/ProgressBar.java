package de.gruwie.music.helper;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.Gruwie_Startup;
import de.gruwie.music.Queue;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;

public class ProgressBar implements Runnable {

	private Message queue_view;
	private Queue queue;
	private AudioTrack track;
	
	private long last_position;
	
	public ProgressBar(Message queue_view, AudioTrack track) {
		this.queue_view = queue_view;
		this.queue = Gruwie_Startup.INSTANCE.getPlayerManager().getController(queue_view.getGuild().getIdLong()).getQueue();
		this.track = track;
	}

	@Override
	public void run() {
		try {
			while(!Thread.currentThread().isInterrupted()) {
				Thread.sleep(ConfigManager.getInteger("refresh") * 1000);
				if(last_position != track.getPosition()) editMessage();
				last_position = track.getPosition();
			}
		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}
	}
	
	public synchronized void editMessage() {
		MessageManager.editMessage(queue_view, queue.toString() + listToString());
	}
	
	private boolean shown = false;
	private int counter = 0;
	private String listToString() {
		StringBuilder b = new StringBuilder("\n");
		if(shown) b.append(":arrow_forward:"); 
		else b.append(":black_medium_square:");
		counter++;
		if(counter >= 2) {
			shown = !shown;
			counter = 0;
		}
		b.append(" " + Formatter.formatTime(track.getPosition()) + "/" + Formatter.formatTime(track.getDuration()));
		b.append(" `" + checkProcess() + "`");
		return b.toString();
	}
	
	private String checkProcess() {
		
		StringBuilder b = new StringBuilder("");
		
		double percentage = ((track.getPosition() + 0.0) / track.getDuration());
		int progress = (int) Math.ceil((percentage * ConfigManager.getInteger("accuracy")));
		
		for (int i = 0; i < progress; i++) b.append("█");
		for (int i = 0; i < ConfigManager.getInteger("accuracy") - progress; i++) b.append(" ‎");
		
		return b.toString();
		
	}
}
