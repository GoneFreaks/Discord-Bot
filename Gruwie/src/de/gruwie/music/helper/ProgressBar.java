package de.gruwie.music.helper;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.Gruwie_Startup;
import de.gruwie.music.Queue;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;

public class ProgressBar implements Runnable {

	private List<String> progress_list;
	private Message queue_view;
	private Queue queue;
	private AudioTrack track;
	
	public ProgressBar(Message queue_view, AudioTrack track) {
		this.queue_view = queue_view;
		this.queue = Gruwie_Startup.INSTANCE.getPlayerManager().getController(queue_view.getGuild().getIdLong()).getQueue();
		this.progress_list = new ArrayList<>();
		this.track = track;
	}

	@Override
	public void run() {
		try {
			while(!Thread.currentThread().isInterrupted()) {
				Thread.sleep(ConfigManager.getInteger("refresh") * 1000);
				checkProcess();
				editMessage();
			}
		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}
	}
	
	public synchronized void editMessage() {
		MessageManager.editMessage(queue_view, queue.toString() + listToString());
	}
	
	private String listToString() {
		StringBuilder b = new StringBuilder("\n");
		b.append(Formatter.formatTime(track.getPosition()));
		b.append(" `");
		for (String i : progress_list) {
			b.append(i);
		}
		for (int i = 0; i < ConfigManager.getInteger("accuracy") - progress_list.size(); i++) {
			b.append(" ");
		}
		b.append("`");
		return b.toString();
	}
	
	private void checkProcess() {
		
		double percentage = ((track.getPosition() + 0.0) / track.getDuration());
		int progress = (int) Math.ceil((percentage * ConfigManager.getInteger("accuracy")));
		
		if(progress != progress_list.size()) {
			for (int i = 0; i < progress - progress_list.size(); i++) {
				progress_list.add("█");
			}
		}
		
	}
}
