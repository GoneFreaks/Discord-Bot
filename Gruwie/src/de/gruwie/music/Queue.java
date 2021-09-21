package de.gruwie.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.gruwie.db.DataClass;
import de.gruwie.util.Formatter;
import de.gruwie.util.MessageManager;

public class Queue {

	private static final int MAX_SIZE = 25;
	private static final int BLOCK_SIZE = 25;
	
	private MusicController controller;
	private AudioPlayer audioPlayer;
	private List<AudioTrack> queuelist;
	private int pointer;

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
	
	public List<AudioTrack> getQueueList() {
		return queuelist;
	}
	
	private void printQueue(long duration) {
		
		StringBuilder b = new StringBuilder("");
		
		b.append("**Queue: **\n\n");
		
		for (int i = 0; i < queuelist.size(); i++) {
			if(i == pointer) b.append("**-->** ");
			AudioTrackInfo info = queuelist.get(i).getInfo();
			b.append(info.title + " ");
			b.append(" **" + Formatter.formatTime(info.length) + "**");
			b.append("\n");
		}
		
		MessageManager.sendEmbedMessage(b.toString(), DataClass.getChannel(controller.getGuild().getIdLong()), duration);
	}
	
}
