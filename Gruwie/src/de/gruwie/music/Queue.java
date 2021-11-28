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
	
	public boolean next() {
		
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

	public void addTrackToQueue(AudioTrack track) {

		if (queuelist.size() >= ConfigManager.getInteger("max_queue_size")) return;
		
		for (AudioTrack i : queuelist) {
			if(i.getInfo().title.equals(track.getInfo().title)) return;
		}
		
		this.queuelist.add(track);
		
		if(view != null) editQueueMessage();

		if (audioPlayer.getPlayingTrack() == null) next();
	}
	
	public void addPlaylistToQueue(List<AudioTrack> tracks) {
		
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
	
	private static final int SIZE = 10;
	@Override
	public String toString() {
		
		StringBuilder b = new StringBuilder("");
		
		b.append("__**Queue: **__\n");
		b.append("Current Filter: *" + filter.getCurrentFilter() + "*\n");
		b.append("Volume: *" + audioPlayer.getVolume() + "*\n");
		b.append(queuelist.size() + "/" + ConfigManager.getInteger("max_queue_size") + " Songs\n\n");
		
		int current_track_index = queuelist.indexOf(current_track);
		
		if(queuelist.size() <= SIZE) b.append(toStringHelper(0, queuelist.size(), current_track_index));
		else {
			int end = (int) Math.min(queuelist.size(), current_track_index + SIZE);
			int start = current_track_index < 0? 0 : current_track_index;
			if(end - current_track_index < SIZE) start -= SIZE - (end - current_track_index);
			
			if(start != 0) b.append("**:arrow_up: " + start + " Track" + (start > 1? "s" : "") + "**\n\n");
			b.append(toStringHelper(start, end, current_track_index));
			if(end != queuelist.size()) b.append("\n**:arrow_down: " + (queuelist.size()-end) + " Track" + ((queuelist.size()-end) > 1? "s" : "") + "**");
		}
		if(queuelist.size() == 0) b.append("**THE QUEUE IS EMPTY**\n");
		
		b.append("\n\nLooping is **" + (repeat? "active" : "not active") + "**");
		return b.toString();
	}
	
	private static final int TITLE_SIZE = 55;
	public String toStringHelper(int start, int end, int current_track_index) {
		StringBuilder b = new StringBuilder("");
		for (int i = start; i < end; i++) {
			if(i == current_track_index) b.append("***:arrow_right:*** ");
			else b.append(":black_small_square: ");
			AudioTrackInfo info = queuelist.get(i).getInfo();
			String title = info.title;
			if(title.length() > TITLE_SIZE) b.append(title.substring(0, TITLE_SIZE) + "...");
			else b.append(title + "");
			b.append(" **" + Formatter.formatTime(info.length) + "**");
			b.append("\n");
		}
		return b.toString();
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
	
	public int size () {
		return queuelist.size();
	}
}