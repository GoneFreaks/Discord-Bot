package de.gruwie.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.gruwie.music.helper.FilterManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.dto.ViewDTO;

public class Queue {
	
	private ViewDTO view;
	
	private AudioPlayer audioPlayer;
	private List<AudioTrack> queuelist;
	private boolean repeat;
	private AudioTrack current_track;
	private FilterManager filter;

	public Queue(MusicController controller) {
		this.audioPlayer = controller.getPlayer();
		this.queuelist = new ArrayList<>();
		this.repeat = ConfigManager.getBoolean("repeat");
		this.filter = controller.getFilterManager();
	}
	
	public void shuffle() {
		Collections.shuffle(queuelist);
		editQueueMessage();
	}
	
	public boolean next() throws Exception {
		
		if(queuelist.size() > 0) {
			
			int next_track = queuelist.indexOf(current_track);
			if(next_track < 0 || next_track+1 >= queuelist.size()) next_track = 0;
			else next_track++;
			
			audioPlayer.stopTrack();
			
			AudioTrack track = repeat? queuelist.get(next_track) : queuelist.remove(0);
			
			if(track != null) {
				current_track = track;
				audioPlayer.playTrack(track.makeClone());
				audioPlayer.setVolume(ConfigManager.getInteger("default_volume"));
				return true;
			}
		}
		return false;
	}
	
	public void setView(ViewDTO view) {
		this.view = view;
	}

	public void addTrackToQueue(AudioTrack track) throws Exception {

		if (queuelist.size() >= ConfigManager.getInteger("max_queue_size")) return;
		
		for (AudioTrack i : queuelist) {
			if(i.getInfo().title.equals(track.getInfo().title)) return;
		}
		
		this.queuelist.add(track);
		
		if(view != null) editQueueMessage();

		if (audioPlayer.getPlayingTrack() == null) next();
	}
	
	public void addPlaylistToQueue(List<AudioTrack> tracks) throws Exception {
		
		for (AudioTrack i : tracks) {
			if(queuelist.size() < ConfigManager.getInteger("max_queue_size")) {
				for (AudioTrack j : queuelist) {
					if(j.getInfo().title.equals(i.getInfo().title)) break;
				}
				queuelist.add(i);
			}
			else break;
		}
		
		if(view != null) editQueueMessage();

		if (audioPlayer.getPlayingTrack() == null) next();
		
	}
	
	public void clearQueue() {
		this.queuelist = new ArrayList<>();
		editQueueMessage();
	}
	
	public List<AudioTrack> getQueueList() {
		return queuelist;
	}
	
	public AudioTrack getCurrentTrack() {
		return current_track;
	}
	
	public void changeRepeat() {
		repeat = !repeat;
		if(!repeat) queuelist.remove(current_track);
		else queuelist.add(current_track);
		editQueueMessage();
	}
	
	public void editQueueMessage() {
		if(view != null) view.editCurrentQueueView(this.toString());
	}
	
	@Override
	public String toString() {
		
		StringBuilder strBuilder = new StringBuilder("");
		
		strBuilder.append("__**Queue: **__\n");
		strBuilder.append("Current Filter: *" + filter.getCurrentFilter() + "*\n");
		strBuilder.append(queuelist.size() + "/" + ConfigManager.getInteger("max_queue_size") + " Songs\n\n");
		
		int current_track_index = queuelist.indexOf(current_track);
		
		if(queuelist.size() <= 25) strBuilder.append(toStringHelper(0, queuelist.size(), current_track_index));
		else {
			int end = (int) Math.min(queuelist.size(), current_track_index + 25);
			int start = current_track_index;
			if(end - current_track_index < 25) start -= 25 - (end - current_track_index);
			
			if(start != 0) strBuilder.append("**:arrow_up: " + start + " Track" + (start > 1? "s" : "") + "**\n\n");
			strBuilder.append(toStringHelper(start, end, current_track_index));
			if(end != queuelist.size()) strBuilder.append("\n**:arrow_down: " + (queuelist.size()-end) + " Track" + ((queuelist.size()-end) > 1? "s" : "") + "**");
		}
		if(queuelist.size() == 0) strBuilder.append("**THE QUEUE IS EMPTY**\n");
		
		strBuilder.append("\n\nLooping is **" + (repeat? "active" : "not active") + "**");
		return strBuilder.toString();
	}
	
	private String toStringHelper(int start, int end, int current_track_index) {
		StringBuilder strBuilder = new StringBuilder("");
		for (int i = start; i < end; i++) {
			if(i == current_track_index) strBuilder.append("***:arrow_right:*** ");
			else strBuilder.append(":black_small_square: ");
			AudioTrackInfo info = queuelist.get(i).getInfo();
			strBuilder.append(info.title + " ");
			strBuilder.append("**" + Formatter.formatTime(info.length) + "**");
			strBuilder.append("\n");
		}
		return strBuilder.toString();
	}
	
	public boolean removeTrack (AudioTrack track) {
		boolean result = queuelist.remove(track);
		editQueueMessage();
		return result;
	}
	
	public boolean removeTrack (String track) {
		
		if(track.equals("")) return removeTrack(current_track);
		
		for (AudioTrack i : queuelist) {
			String title = i.getInfo().title;
			if(track.equals(title)) return removeTrack(i);
		}
		return false;
	}
}
