package de.gruwie.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.music.Queue;
import net.dv8tion.jda.api.entities.Message;

public class ProgressBar implements Runnable {

	private final Queue queue;
	private final AudioPlayer player;
	
	public ProgressBar(Queue queue, AudioPlayer player) {
		GruwieUtilities.log();
		this.queue = queue;
		this.player = player;
	}

	@Override
	public void run() {
		GruwieUtilities.log();
		while(true) {
			try {
				Thread.sleep(ConfigManager.getRefreshTimer() * 1000);
				AudioTrack current = queue.getCurrentTrack();
				if(current != null) editMessage(current, false);
			} catch (Exception e) {
			}
		}
	}
	
	public void editMessage(AudioTrack current, boolean manual) {
		if(manual || (!player.isPaused() && current.getDuration() < 1_800_000)) {
			Message m = queue.getQueueView();
			
			if(m != null) {
				String progressbar = queue.isStream()? ":red_circle: Stream" : listToString(current.getPosition(), current.getDuration());
				MessageManager.editMessage(queue.getQueueView(), queue.toString() + progressbar);
			}
		}
	}
	
	private String listToString(long current_position, long duration) {
		StringBuilder b = new StringBuilder("");
		b.append("`" + checkProcess(current_position, duration) + "`");
		b.append(" " + GruwieUtilities.formatTime(current_position) + "/" + GruwieUtilities.formatTime(duration));
		return b.toString();
	}
	
	private String checkProcess(long current_position, long duration) {
		
		StringBuilder b = new StringBuilder("");
		
		double percentage = ((current_position + 0.0) / duration);
		int progress = (int) Math.ceil((percentage * 53));
		
		for (int i = 0; i < progress; i++) b.append("█");
		for (int i = 0; i < 53 - progress; i++) b.append(" ‎");
		
		return b.toString();
	}
}
