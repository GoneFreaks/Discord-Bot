package de.gruwie.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.dto.ViewDTO;

public class Queue {
	
	private ViewDTO view;
	
	private AudioPlayer audioPlayer;
	private List<AudioTrack> queuelist;
	private boolean repeat;
	private AudioTrack current_track;

	public Queue(MusicController controller) {
		this.audioPlayer = controller.getPlayer();
		this.queuelist = new ArrayList<>();
		this.repeat = ConfigManager.getBoolean("repeat");
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
		
		queuelist = tracks;
		
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
	
	public String getCurrentTrackTitle() {
		return current_track.getInfo().title;
	}
	
	public void changeRepeat() {
		repeat = !repeat;
		if(!repeat) queuelist.remove(current_track);
		editQueueMessage();
	}
	
	public void editQueueMessage() {
		if(view != null) view.editCurrentQueueView(this.toString());
	}

	@Override
	public String toString() {
		
		if(queuelist.size() == 0) return "**THE QUEUE IS EMPTY**";
		
		StringBuilder b = new StringBuilder("");
		
		b.append("__**Queue: **__\n");
		b.append(queuelist.size() + "/" + ConfigManager.getInteger("max_queue_size") + " Songs\n\n");
		int next_track = queuelist.indexOf(current_track);
		
		for (int i = 0; i < queuelist.size(); i++) {
			if(i == next_track) b.append("***:arrow_right:*** ");
			else b.append(":black_small_square: ");
			AudioTrackInfo info = queuelist.get(i).getInfo();
			b.append(info.title + " ");
			b.append("**" + Formatter.formatTime(info.length) + "**");
			b.append("\n");
			
		}
		
		b.append("\n\nLooping is **" + (repeat? "active" : "not active") + "**");
		return b.toString();
	}
	
	public List<String> createStrings() {
		if(queuelist.size() == 0) return null;
		
		StringBuilder b = new StringBuilder("");
		
		b.append("__**Queue: **__\n");
		b.append(queuelist.size() + "/" + ConfigManager.getInteger("max_queue_size") + " Songs\n\n");
		int next_track = queuelist.indexOf(current_track);
		
		List<String> result = new ArrayList<>();
		for (int i = 0; i < queuelist.size(); i++) {
			AudioTrackInfo info = queuelist.get(i).getInfo();
			
			if(b.length() + info.title.length() <= 5000) {
				
			}
			else {
				
			}
			
		}
		for (AudioTrack track : queuelist) {
			String i = track.getInfo().title;
			if(b.length() + i.length() <= 5000) {
				b.append(i);
			}
			else {
				result.add(b.toString());
				b = new StringBuilder("");
				b.append(i);
			}
		}
		result.add(b.toString());
		return result;
	}
	
	public StringBuilder getElement(int i, int next_track, StringBuilder b, AudioTrackInfo info) {
		if(i == next_track) b.append("***:arrow_right:*** ");
		else b.append(":black_small_square: ");
		
		b.append(info.title + " ");
		b.append("**" + Formatter.formatTime(info.length) + "**");
		b.append("\n");
		
		return b;
	}
	
	public boolean removeTrack (AudioTrack track) {
		boolean result = queuelist.remove(track);
		editQueueMessage();
		return result;
	}
	
	public boolean removeTrack (String track) {
		for (AudioTrack i : queuelist) {
			String title = i.getInfo().title;
			if(track.equals(title)) return removeTrack(i);
		}
		return false;
	}
}
