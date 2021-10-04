package de.gruwie.util.dto;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class CheckTrackDTO implements Comparable<CheckTrackDTO> {

	private int treffer;
	private String title;
	
	private AudioTrack track;
	
	public CheckTrackDTO(AudioTrack track) {
		this.track = track;
		this.title = track.getInfo().title.toLowerCase();
		this.treffer = 0;
	}
	
	public void addTreffer() {
		this.treffer++;
	}
	
	public int getTreffer() {
		return this.treffer;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public AudioTrack getTrack() {
		return this.track;
	}
	
	@Override
	public int compareTo(CheckTrackDTO o) {
		return (this.treffer > o.treffer)? -1 : 1;
	}
	
}