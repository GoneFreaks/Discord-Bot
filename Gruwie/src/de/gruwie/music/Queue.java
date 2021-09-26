package de.gruwie.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.gruwie.db.DataManager;
import de.gruwie.util.Formatter;
import de.gruwie.util.MessageManager;

public class Queue {

	private static final int MAX_SIZE = 100;
	private static final int BLOCK_SIZE = 25;
	
	private MusicController controller;
	private AudioPlayer audioPlayer;
	private List<AudioTrack> queuelist;
	private int pointer;
	
	private AudioTrack current;

	public Queue(MusicController controller) {
		this.controller = controller;
		this.audioPlayer = controller.getPlayer();
		this.queuelist = new ArrayList<>();
		pointer = 0;
	}
	
	public boolean next() {
	
		if(queuelist.size() > 0) {
			
			if(audioPlayer.getPlayingTrack() != null) {
				audioPlayer.stopTrack();
			}
			
			if(pointer >= queuelist.size()) pointer = 0;
			
			AudioTrack track = queuelist.get(pointer);
			
			if(track != null) {
				current = track;
				audioPlayer.playTrack(track.makeClone());
				printQueue(track.getDuration() + 10000);
				pointer++;
				return true;
			}
		}
		return false;
	}

	public void addTrackToQueue(AudioTrack track) {

		if (queuelist.size() >= MAX_SIZE) queuelist.remove(0);
		this.queuelist.add(track);

		if (controller.getPlayer().getPlayingTrack() == null) next();
	}
	
	public void deleteQueue() {
		this.queuelist = new ArrayList<>();
	}
	
	public List<AudioTrack> getQueueList() {
		return queuelist;
	}
	
	private void printQueue(long duration) {
		
		StringBuilder strBuilder = new StringBuilder("");
		
		strBuilder.append("__**Queue: **__\n");
		strBuilder.append(queuelist.size() + "/" + MAX_SIZE + " Songs\n\n");
		
		for (int i = 0; i < queuelist.size(); i++) {
			if(i == pointer) strBuilder.append("***:arrow_right:*** ");
			else strBuilder.append(":black_small_square: ");
			AudioTrackInfo info = queuelist.get(i).getInfo();
			strBuilder.append(info.title + " ");
			strBuilder.append("**" + Formatter.formatTime(info.length) + "**");
			strBuilder.append("\n");
			
			if((i+1) % BLOCK_SIZE == 0) {
				MessageManager.sendEmbedMessage(strBuilder.toString(), DataManager.getChannel(controller.getGuild().getIdLong()), duration);
				strBuilder = new StringBuilder("");
			}
			
		}
		
		MessageManager.sendEmbedMessage(strBuilder.toString(), DataManager.getChannel(controller.getGuild().getIdLong()), duration);
	}
	
}
