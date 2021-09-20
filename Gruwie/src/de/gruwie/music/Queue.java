package de.gruwie.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class Queue {

	private static final int MAX_SIZE = 50;
	
	private MusicController controller;
	private List<AudioTrack> queuelist;
	private int pointer;

	public Queue(MusicController controller) {
		this.controller = controller;
		this.queuelist = new ArrayList<>();
		pointer = 0;
	}
	
	public boolean next() {
		
		if(queuelist.size() > 0) {
			AudioTrack track = queuelist.get(pointer++);
			
			if(track != null) {
				this.controller.getPlayer().playTrack(track);
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
	
}
