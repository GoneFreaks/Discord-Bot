package de.gruwie.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.gruwie.db.DataManager;
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
	private Message current_queue;

	public Queue(MusicController controller) {
		this.controller = controller;
		this.audioPlayer = controller.getPlayer();
		this.queuelist = new ArrayList<>();
		pointer = 0;
		pointer_current = 0;
	}
	
	public boolean next() throws Exception {
	
		if(queuelist.size() > 0) {
			
			if(pointer >= queuelist.size()) pointer = 0;
			
			AudioTrack track = repeat? queuelist.get(pointer) : queuelist.remove(0);
			pointer_current = pointer;
			
			if(track != null) {
				current_track = track;
				audioPlayer.playTrack(track.makeClone());
				printQueue(track.getDuration() + 10000);
				if(repeat) pointer++;
				return true;
			}
		}
		return false;
	}

	public void addTrackToQueue(AudioTrack track) throws Exception {

		if (queuelist.size() >= MAX_SIZE) return;
		
		this.queuelist.add(track);
		
		editQueueMessage();

		if (controller.getPlayer().getPlayingTrack() == null) next();
	}
	
	public void deleteQueue() {
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
	}
	
	private String queueToString() {
		
		if(queuelist.size() == 0) return "**THE QUEUE IS EMPTY**";
		
		StringBuilder strBuilder = new StringBuilder("");
		
		strBuilder.append("__**Queue: **__\n");
		strBuilder.append(queuelist.size() + "/" + MAX_SIZE + " Songs\n\n");
		
		for (int i = 0; i < queuelist.size(); i++) {
			if(i == pointer_current) strBuilder.append("***:arrow_right:*** ");
			else strBuilder.append(":black_small_square: ");
			AudioTrackInfo info = queuelist.get(i).getInfo();
			strBuilder.append(info.title + " ");
			strBuilder.append("**" + Formatter.formatTime(info.length) + "**");
			strBuilder.append("\n");
			
		}
		
		return strBuilder.toString();
	}
	
	private void printQueue(long duration) {
		current_queue = MessageManager.sendEmbedMessage(queueToString(), DataManager.getChannel(controller.getGuild().getIdLong()), duration);
	}
	
	private void editQueueMessage() {
		if(current_queue != null) MessageManager.editMessage(current_queue, queueToString(), current_track.getDuration() - current_track.getPosition());
	}
	
}
