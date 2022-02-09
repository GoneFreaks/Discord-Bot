package de.gruwie.music;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.View;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Message;

public class Queue {
	
	private View view;
	
	private long guild_id;
	
	private MusicController controller;
	private AudioPlayer audioPlayer;
	private List<AudioTrack> queuelist;
	private boolean repeat;
	private AudioTrack current_track;
	private AudioTrack current_track_clone;
	private AudioTrack next_audio_track;
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
		this.guild_id = controller.getGuild().getIdLong();
	}
	
	private long last_updated;
	private void editMessage() {
		if((System.currentTimeMillis() - last_updated) > 1000) {
			controller.getProgressBar().editMessage(current_track_clone);
			last_updated = System.currentTimeMillis();
		}
	}
	
	public void shuffle() {
		Collections.shuffle(queuelist);
		editMessage();
	}
	
	public boolean next() {
		
		if(queuelist.size() > 0) {
			offset = 0;
			int next_track;
			if(next_audio_track != null) next_track = queuelist.indexOf(next_audio_track);
			else next_track = (queuelist.indexOf(current_track) + 1);
			
			if(next_track < 0 || next_track >= queuelist.size()) next_track = 0;
			
			audioPlayer.stopTrack();
			
			AudioTrack track = repeat? queuelist.get(next_track) : queuelist.remove(next_audio_track != null? next_track : 0);
			next_audio_track = null;
			
			if(track != null) {
				current_track = track;
				current_track_clone = track.makeClone();
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
	
	public Message getQueueView () {
		if(view != null) return view.getQueueView();
		else return null;
	}

	public synchronized void addTrackToQueue(AudioTrack track) {

		if (queuelist.size() >= ConfigManager.getInteger("max_queue_size")) return;
		
		for (AudioTrack i : queuelist) {
			if(i.getInfo().title.equals(track.getInfo().title)) return;
		}
		
		this.queuelist.add(track);

		if (audioPlayer.getPlayingTrack() == null) next();
		
		editMessage();
	}
	
	public void addPlaylistToQueue(List<AudioTrack> tracks) {
		
		for (AudioTrack i : tracks) {
			if(queuelist.size() + tracks.size() <= ConfigManager.getInteger("max_queue_size")) {
				boolean already = false;
				for (AudioTrack j : queuelist) {
					if(j.getInfo().title.equals(i.getInfo().title)) {
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
		this.queuelist = new LinkedList<>();
		editMessage();
	}
	
	public List<AudioTrack> getQueueList() {
		return queuelist;
	}
	
	public AudioTrack getCurrentTrack() {
		return current_track_clone;
	}
	
	public void changeRepeat() {
		repeat = !repeat;
		if(!repeat) queuelist.remove(current_track);
		else queuelist.add(current_track);
		shuffle();
	}
	
	@Override
	public String toString() {
		
		StringBuilder b = new StringBuilder("");
		int size = ConfigManager.getInteger("queue_show");
		
		b.append("__**Queue: **__\n");
		b.append("Current Filter: *" + filter.getCurrentFilter() + "*\n");
		b.append("Looping: " + (repeat? "Active" : "Not active") + "\n");
		b.append(queuelist.size() + "/" + ConfigManager.getInteger("max_queue_size") + " Songs\n");
		String temp = queueTime();
		if(temp != null) b.append("Duration: *" + temp +"*");
		b.append("\n\n");
		
		int current_track_index = queuelist.indexOf(current_track);
		if(current_track_index < 0) current_track_index = 0;
		
		if(queuelist.size() > 0) b.append(Formatter.getBorder(63, "⎯"));
		if(queuelist.size() <= size) b.append(toStringHelper(0, queuelist.size()));
		else {
			
			int start = offset < 0? 0 : offset;
			int end = ((int) Math.min(queuelist.size(), start + size));
			if(end - start < size) start -= size - (end - start);
			if(start != 0) b.append("**🠉 " + start + " Track" + (start > 1? "s" : "") + "**\n");
			b.append(toStringHelper(start, end));
			if(end != queuelist.size()) b.append("**🠋 " + (queuelist.size()-end) + " Track" + ((queuelist.size()-end) > 1? "s" : "") + "**\n");
		}
		if(queuelist.size() > 0) b.append(Formatter.getBorder(63, "⎯"));
		return b.toString();
	}
	
	public String toStringHelper(int start, int end) {
		
		if(end == 0) return "**THE QUEUE IS EMPTY**\n";
		
		StringBuilder b = new StringBuilder("");
		int title_size = ConfigManager.getInteger("queue_character_count");
		for (int i = start; i < end; i++) {
			AudioTrack j = queuelist.get(i);
			
			if(j.equals(current_track) && repeat) b.append(":arrow_right: ");
			else if(j.equals(next_audio_track)) b.append(":arrow_right_hook: ");
			else b.append(":black_small_square: ");
			
			String title = j.getInfo().title.replaceAll("\\*", " ");
			if(title.length() > title_size) b.append(title.substring(0, title_size) + "...");
			else b.append(title);
			b.append(" **" + Formatter.formatTime(j.getInfo().length) + "**");
			b.append("\n");
		}
		return b.toString();
	}
	
	public boolean removeTrack (AudioTrack track) {
		boolean result = queuelist.remove(track);
		editMessage();
		return result;
	}
	
	public boolean removeTrack (String track) {
		
		boolean result = false;
		
		for (AudioTrack i : queuelist) {
			String title = i.getInfo().title;
			if(track.equals(title)) result = removeTrack(i);
		}
		editMessage();
		return result;
	}
	
	public int size () {
		return queuelist.size();
	}
	
	private String queueTime() {
		long sum = 0;
		for (AudioTrack i : queuelist) {
			sum += i.getDuration();
		}
		if(sum < 0) return null;
		else return Formatter.formatTime(sum);
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
		if(next_audio_track == null) {
			for (AudioTrack i : queuelist) {
				if(i.getInfo().title.equals(track)) {
					this.next_audio_track = i;
					queuelist.remove(i);
					if(repeat) queuelist.add(queuelist.indexOf(current_track) + 1, i);
					else queuelist.add(0, i);
					break;
				}
			}
		}
		else MessageManager.sendEmbedMessage(true, "**THERE'S ALREADY A NEXT TRACK**", guild_id, null);
		editMessage();
	}
}