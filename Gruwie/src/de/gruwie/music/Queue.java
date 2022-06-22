package de.gruwie.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.util.ConcurrentLinkedList;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.View;
import de.gruwie.util.dto.AudioTrackTimed;
import net.dv8tion.jda.api.entities.Message;

public class Queue {
	
	private ConcurrentLinkedList<AudioTrackTimed> queuelist;
	
	private View view;
	private MusicController controller;
	private AudioPlayer audioPlayer;
	private boolean repeat;
	private AudioTrackTimed current_track;
	private AudioTrack current_track_clone;
	private FilterManager filter;
	int offset;

	public Queue(MusicController controller) {
		this.controller = controller;
		this.audioPlayer = controller.getPlayer();
		this.queuelist = new ConcurrentLinkedList<>();
		this.repeat = ConfigManager.getBoolean("repeat");
		this.filter = controller.getFilterManager();
		this.offset = 0;
	}
	
	public void editMessage() {
		controller.getProgressBar().editMessage(current_track_clone, true);
	}
	
	public void shuffle() {
		queuelist.shuffle();
		queuelist.remove(current_track);
		queuelist.addFirst(current_track);
		editMessage();
	}
	
	public boolean next() {
		
		if(queuelist.size() > 0) {
			
			audioPlayer.stopTrack();
			
			if(repeat) queuelist.add(queuelist.remove(0));
			
			AudioTrackTimed track = repeat? queuelist.get(0) : queuelist.remove(0);
			
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

	public void addTrackToQueue(AudioTrackTimed track) {

		if (queuelist.size() >= ConfigManager.getInteger("max_queue_size")) return;
		
		for (AudioTrackTimed i : queuelist.getContentCopy()) {
			if(i.getTitle().equals(track.getTitle())) return;
		}
		
		this.queuelist.add(track);

		if (audioPlayer.getPlayingTrack() == null) next();
		
		editMessage();
	}
	
	public void addPlaylistToQueue(List<AudioTrackTimed> tracks) {
		
		for (AudioTrackTimed i : tracks) {
			if(queuelist.size() < ConfigManager.getInteger("max_queue_size")) {
				boolean already = false;
				for (AudioTrackTimed j : queuelist.getContentCopy()) {
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
	
	public void clearQueue() {
		this.queuelist = new ConcurrentLinkedList<>();
		editMessage();
	}
	
	public List<AudioTrack> getQueueList() {
		return AudioTrackTimed.convertToNonTimed(queuelist.getContentCopy());
	}
	
	public AudioTrack getCurrentTrack() {
		return current_track_clone;
	}
	
	public void changeRepeat() {
		repeat = !repeat;
		if(!repeat) queuelist.remove(current_track);
		else if(current_track != null) {
			queuelist.addFirst(current_track);
		}
		editMessage();
	}
	
	@Override
	public String toString() {
		
		StringBuilder b = new StringBuilder("");
		int size = ConfigManager.getInteger("queue_show");
		
		b.append("__**Queue: **__\n");
		b.append("Current Filter: *" + filter.getCurrentFilter() + "*⠀⠀⠀⠀⠀⠀");
		b.append(queuelist.size() + "/" + ConfigManager.getInteger("max_queue_size") + " Songs" + (repeat? " 🔁" : "") + "⠀⠀⠀⠀⠀⠀");
		String temp = queueTime();
		if(temp != null) b.append("Duration: *" + temp +"*");
		b.append("\n");
		
		if(queuelist.size() <= size) b.append(toStringHelper(0, queuelist.size(), -1));
		else b.append(toStringHelper(offset, size + offset, -1));
		
		b.append("**" + GruwieUtilities.getBorder(63, "⎯") + "**");
		return b.toString();
	}
	
	public StringBuilder toStringHelper(int start, int end, int custom_character_count) {
		
		StringBuilder b = new StringBuilder("");
		
		if(end != 0) b.append("```fix\n");
		
		int title_size = custom_character_count > 0? custom_character_count : ConfigManager.getInteger("queue_character_count");
		for (int i = start; i < end; i++) {
			AudioTrackTimed j = queuelist.get(i);
			
			if(j.equals(current_track) && repeat) b.append("➡️ ");
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
	
	private boolean removeTrack (AudioTrackTimed track) {
		boolean result = queuelist.remove(track);
		editMessage();
		return result;
	}
	
	public boolean removeTrack (String track) {
		queuelist.lock();
		boolean result = false;
		
		for (AudioTrackTimed i : queuelist.getContentCopy()) {
			String title = i.getTitle();
			if(track.equals(title)) result = removeTrack(i);
		}
		queuelist.unlock();
		editMessage();
		return result;
	}
	
	public int size () {
		return queuelist.size();
	}
	
	private String queueTime() {
		queuelist.lock();
		long sum = 0;
		for (AudioTrackTimed i : queuelist.getContentCopy()) {
			sum += i.getLength();
		}
		queuelist.unlock();
		if(sum < 0) return null;
		else return GruwieUtilities.formatTime(sum);
	}
	
	public void moveQueueUp() {
		move(-1);
	}
	
	public void moveQueueDown() {
		move(1);
	}
	
	private void move(int sign) {
		int temp = offset;
		temp += sign * ConfigManager.getInteger("queue_show");
		if(temp < queuelist.size() && temp >= 0) offset = temp;
		editMessage();
	}
	
	public void setNextTrack(String track) {
		queuelist.lock();
		for (AudioTrackTimed i : queuelist.getContentCopy()) {
			if(i.getTitle().equals(track)) {
				queuelist.remove(i);
				queuelist.addFirst(i);
				break;
			}
		}
		offset = 0;
		queuelist.unlock();
		editMessage();
	}
	
	public boolean isStream() {
		return current_track_clone.getInfo().isStream;
	}
}