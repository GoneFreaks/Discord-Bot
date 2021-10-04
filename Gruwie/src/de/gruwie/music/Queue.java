package de.gruwie.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.gruwie.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.dto.ViewDTO;

public class Queue {

	private static int MAX_SIZE;
	
	private ViewDTO view;
	
	private MusicController controller;
	private AudioPlayer audioPlayer;
	private List<AudioTrack> queuelist;
	private int pointer;
	private int pointer_current;
	private boolean repeat = true;
	
	private AudioTrack current_track;

	public Queue(MusicController controller) {
		this.controller = controller;
		this.audioPlayer = controller.getPlayer();
		this.queuelist = new ArrayList<>();
		pointer = 0;
		pointer_current = 0;
		MAX_SIZE = ConfigManager.getInteger("max_queue_size");
	}
	
	public boolean next() throws Exception {
		
		if(audioPlayer.getPlayingTrack() != null) audioPlayer.setVolume(0);
		
		if(queuelist.size() > 0) {
			
			if(pointer >= queuelist.size()) pointer = 0;
			
			AudioTrack track = repeat? queuelist.get(pointer) : queuelist.remove(0);
			pointer_current = pointer;
			
			if(track != null) {
				current_track = track;
				audioPlayer.setVolume(ConfigManager.getInteger("default_volume"));
				audioPlayer.playTrack(track.makeClone());
				if(repeat) pointer++;
				return true;
			}
		}
		return false;
	}
	
	public void setView(ViewDTO view) {
		this.view = view;
	}

	public void addTrackToQueue(AudioTrack track) throws Exception {

		if (queuelist.size() >= MAX_SIZE) return;
		
		this.queuelist.add(track);
		
		if(view != null) editQueueMessage();

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
	
	private void editQueueMessage() {
		view.editCurrentQueueView(this.toString());
	}

	@Override
	public String toString() {
		
		if(queuelist.size() == 0) return "**THE QUEUE IS EMPTY**";
		
		StringBuilder strBuilder = new StringBuilder("");
		
		strBuilder.append("__**Queue: **__\n");
		strBuilder.append(queuelist.size() + "/" + MAX_SIZE + " Songs\n");
		strBuilder.append("Current Player-Volume: " + audioPlayer.getVolume() + "\n\n");
		
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
	
	public boolean removeTrack (AudioTrack track) {
		boolean result = queuelist.remove(track);
		editQueueMessage();
		return result;
	}
	
	public void changeVolume(int volume) {
		if(volume > 100) volume = 100;
		if(volume < 0) volume = 0;
		audioPlayer.setVolume(volume);
		editQueueMessage();
	}
	
}
