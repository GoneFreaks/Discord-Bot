package de.gruwie.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.gruwie.util.Formatter;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;

public class Queue {

	private static final int MAX_SIZE = 25;
	
	private MusicController controller;
	private AudioPlayer audioPlayer;
	private List<AudioTrack> queuelist;
	private int pointer;
	private int pointer_current;
	private boolean repeat = true;
	
	private AudioTrack current_track;
	private Message current_queue_view;
	
	private static String[] emote_arr = {"⏹️", "⏸️", "▶️", "⏭️", "🔁", "🆕"};

	public Queue(MusicController controller) {
		this.controller = controller;
		this.audioPlayer = controller.getPlayer();
		this.queuelist = new ArrayList<>();
		pointer = 0;
		pointer_current = 0;
	}
	
	public boolean next() throws Exception {
		
		if(audioPlayer.getPlayingTrack() != null) audioPlayer.setVolume(0);
		
		if(queuelist.size() > 0) {
			
			if(pointer >= queuelist.size()) pointer = 0;
			
			AudioTrack track = repeat? queuelist.get(pointer) : queuelist.remove(0);
			pointer_current = pointer;
			
			if(track != null) {
				current_track = track;
				audioPlayer.playTrack(track.makeClone());
				audioPlayer.setVolume(25);
				if(repeat) pointer++;
				return true;
			}
		}
		return false;
	}
	
	public void setView (Message current_queue_view) {
		this.current_queue_view = current_queue_view;
		
		for (int i = 0; i < emote_arr.length; i++) {
			if(current_queue_view != null)
			current_queue_view.addReaction(emote_arr[i]).queue();
		}
	}

	public void addTrackToQueue(AudioTrack track) throws Exception {

		if (queuelist.size() >= MAX_SIZE) return;
		
		this.queuelist.add(track);
		
		editQueueMessage();

		if (controller.getPlayer().getPlayingTrack() == null) next();
	}
	
	public void clearQueue() {
		this.queuelist = new ArrayList<>();
		editQueueMessage();
	}
	
	public List<AudioTrack> getQueueList() {
		return queuelist;
	}
	
	public String getCurrentTrackTitle() {
		return current_track.getInfo().title;
	}
	
	public void changeRepeat() {
		repeat = !repeat;
		editQueueMessage();
	}
	
	@Override
	public String toString() {
		
		if(queuelist.size() == 0) return "**THE QUEUE IS EMPTY**";
		
		StringBuilder strBuilder = new StringBuilder("");
		
		strBuilder.append("__**Queue: **__\n");
		strBuilder.append(queuelist.size() + "/" + MAX_SIZE + " Songs\n\n");
		
		for (int i = 0; i < queuelist.size(); i++) {
			if(repeat && i == pointer_current) strBuilder.append("***:arrow_right:*** ");
			else strBuilder.append(":black_small_square: ");
			AudioTrackInfo info = queuelist.get(i).getInfo();
			strBuilder.append(info.title + " ");
			strBuilder.append("**" + Formatter.formatTime(info.length) + "**");
			strBuilder.append("\n");
			
		}
		
		strBuilder.append("\n\nLooping is **" + (repeat? "active" : "not active") + "**");
		
		return strBuilder.toString();
	}
	
	private void editQueueMessage() {
		if(current_queue_view != null) MessageManager.editMessage(current_queue_view, this.toString());
	}
	
}
