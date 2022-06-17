package de.gruwie.music;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.View;
import de.gruwie.util.dto.AudioTrackTimed;
import net.dv8tion.jda.api.entities.Message;

public class Queue {
	
	private View view;
	
	private MusicController controller;
	private AudioPlayer audioPlayer;
	private List<AudioTrackTimed> queuelist;
	private boolean repeat;
	private AudioTrackTimed current_track;
	private AudioTrack current_track_clone;
	private AudioTrackTimed next_audio_track;
	private FilterManager filter;
	private int offset;

	public Queue(MusicController controller) {
		this.controller = controller;
		this.audioPlayer = controller.getPlayer();
		this.queuelist = new LinkedList<>();
		this.repeat = ConfigManager.getBoolean("repeat");
		this.filter = controller.getFilterManager();
		this.offset = 0;
		this.next_audio_track = null;
	}
	
	public void editMessage() {
		controller.getProgressBar().editMessage(current_track_clone, true);
	}
	
	public synchronized void shuffle() {
		Collections.shuffle(queuelist);
		editMessage();
	}
	
	public synchronized boolean next() {
		
		if(queuelist.size() > 0) {
			if(repeat) offset = queuelist.indexOf(current_track) + 1;
			else offset = 0;
			int next_track;
			if(next_audio_track != null) next_track = queuelist.indexOf(next_audio_track);
			else next_track = (queuelist.indexOf(current_track) + 1);
			
			if(next_track < 0 || next_track >= queuelist.size()) next_track = 0;
			
			audioPlayer.stopTrack();
			
			AudioTrackTimed track = repeat? queuelist.get(next_track) : queuelist.remove(next_audio_track != null? next_track : 0);
			next_audio_track = null;
			
			if(track != null) {
				current_track = track;
				current_track_clone = track.getTrackClone();
				audioPlayer.playTrack(current_track_clone);
				audioPlayer.setVolume(ConfigManager.getInteger("default_volume"));
				return true;
			}
		}
		return false;
	}
	
	public void setView(View view) {
		this.view = view;
	}
	
	public View getView() {
		return view;
	}
	
	public Message getQueueView () {
		if(view != null) return view.getQueueView();
		else return null;
	}

	public synchronized void addTrackToQueue(AudioTrackTimed track) {

		if (queuelist.size() >= ConfigManager.getInteger("max_queue_size")) return;
		
		for (AudioTrackTimed i : queuelist) {
			if(i.getTitle().equals(track.getTitle())) return;
		}
		
		this.queuelist.add(track);

		if (audioPlayer.getPlayingTrack() == null) next();
		
		editMessage();
	}
	
	public synchronized void addPlaylistToQueue(List<AudioTrackTimed> tracks) {
		
		for (AudioTrackTimed i : tracks) {
			if(queuelist.size() < ConfigManager.getInteger("max_queue_size")) {
				boolean already = false;
				for (AudioTrackTimed j : queuelist) {
					if(j.getTitle().equals(i.getTitle())) {
						already = true;
						break;
					}
				}
				if(!already) queuelist.add(i);
			}
			else break;
		}
		if (audioPlayer.getPlayingTrack() == null) next();
		
		editMessage();
	}
	
	public synchronized void clearQueue() {
		this.queuelist = new LinkedList<>();
		editMessage();
	}
	
	public synchronized List<AudioTrack> getQueueList() {
		return AudioTrackTimed.convertToNonTimed(queuelist);
	}
	
	public AudioTrack getCurrentTrack() {
		return current_track_clone;
	}
	
	public synchronized void changeRepeat() {
		repeat = !repeat;
		if(!repeat) queuelist.remove(current_track);
		else if(current_track != null) {
			offset = queuelist.indexOf(current_track);
			queuelist.add(current_track);
		}
		shuffle();
	}
	
	@Override
	public synchronized String toString() {
		
		StringBuilder b = new StringBuilder("");
		int size = ConfigManager.getInteger("queue_show");
		
		b.append("__**Queue: **__\n");
		b.append("Current Filter: *" + filter.getCurrentFilter() + "*⠀⠀⠀⠀⠀⠀");
		b.append(queuelist.size() + "/" + ConfigManager.getInteger("max_queue_size") + " Songs" + (repeat? " 🔁" : "") + "⠀⠀⠀⠀⠀⠀");
		String temp = queueTime();
		if(temp != null) b.append("Duration: *" + temp +"*");
		b.append("\n");
		
		int current_track_index = queuelist.indexOf(current_track);
		if(current_track_index < 0) current_track_index = 0;
		
		if(queuelist.size() <= size) b.append(toStringHelper(0, queuelist.size(), -1));
		else {
			
			int start = offset < 0? 0 : offset;
			int end = ((int) Math.min(queuelist.size(), start + size));
			if(end - start < size) start -= size - (end - start);
			if(start != 0) b.append("🠉 " + start + " Track" + (start > 1? "s" : "") + "\n");
			b.append(toStringHelper(start, end, -1));
			if(end != queuelist.size()) b.append("🠋 " + (queuelist.size()-end) + " Track" + ((queuelist.size()-end) > 1? "s" : "") + "\n");
		}
		b.append("**" + GruwieUtilities.getBorder(63, "⎯") + "**");
		return b.toString();
	}
	
	public synchronized StringBuilder toStringHelper(int start, int end, int custom_character_count) {
		
		StringBuilder b = new StringBuilder("");
		
		if(end != 0) b.append("```fix\n");
		
		int title_size = custom_character_count > 0? custom_character_count : ConfigManager.getInteger("queue_character_count");
		for (int i = start; i < end; i++) {
			AudioTrackTimed j = queuelist.get(i);
			
			if(j.equals(current_track) && repeat) b.append("➡️ ");
			else if(j.equals(next_audio_track)) b.append("↪️ ");
			else b.append("⬛ ");
			
			b.append("" + GruwieUtilities.formatTime(j.getLength()) + "⠀⠀");
			String title = j.getTitle();
			if(title.length() > title_size) b.append(title.substring(0, title_size) + "...");
			else b.append(title);
			b.append("\n");
		}

		if(end != 0) b.append("```");
		
		return b;
	}
	
	private synchronized boolean removeTrack (AudioTrackTimed track) {
		if(next_audio_track != null && next_audio_track.equals(track)) next_audio_track = null;
		boolean result = queuelist.remove(track);
		editMessage();
		return result;
	}
	
	public synchronized boolean removeTrack (String track) {
		
		boolean result = false;
		
		for (AudioTrackTimed i : queuelist) {
			String title = i.getTitle();
			if(track.equals(title)) result = removeTrack(i);
		}
		editMessage();
		return result;
	}
	
	public synchronized int size () {
		return queuelist.size();
	}
	
	private synchronized String queueTime() {
		long sum = 0;
		for (AudioTrackTimed i : queuelist) {
			sum += i.getLength();
		}
		if(sum < 0) return null;
		else return GruwieUtilities.formatTime(sum);
	}
	
	public void moveQueueUp() {
		move(-1);
	}
	
	public void moveQueueDown() {
		move(1);
	}
	
	private synchronized void move(int sign) {
		int temp = offset;
		temp += sign * ConfigManager.getInteger("queue_show");
		if(temp < queuelist.size() && temp >= 0) offset = temp;
		editMessage();
	}
	
	public synchronized boolean setNextTrack(String track) {
		if(next_audio_track == null) {
			for (AudioTrackTimed i : queuelist) {
				if(i.getTitle().equals(track)) {
					this.next_audio_track = i;
					queuelist.remove(i);
					if(repeat) queuelist.add(queuelist.indexOf(current_track) + 1, i);
					else queuelist.add(0, i);
					break;
				}
			}
			offset = 0;
			editMessage();
			return true;
		}
		else return false;
	}
	
	public boolean isStream() {
		return current_track_clone.getInfo().isStream;
	}
}