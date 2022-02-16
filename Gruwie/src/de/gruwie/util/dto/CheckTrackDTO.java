package de.gruwie.util.dto;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class CheckTrackDTO implements Comparable<CheckTrackDTO> {

	private int treffer;
	private final String title;
	private final AudioTrack track;
	
	public CheckTrackDTO(AudioTrack track) {
		this.track = track;
		this.title = track.getInfo().title;
		this.treffer = 0;
	}
	
	public void addTreffer() {
		this.treffer++;
	}
	
	public int getTreffer() {
		return this.treffer;
	}
	
	public String getTitle() {
		return this.title.toLowerCase();
	}
	
	public String getTitleOriginal() {
		return this.title;
	}
	
	public AudioTrack getTrack() {
		return this.track;
	}
	
	@Override
	public int compareTo(CheckTrackDTO o) {
		if(this.getTreffer() == o.getTreffer()) return 0;
		if(this.getTreffer() > o.getTreffer()) return 1;
		if(this.getTreffer() < o.getTreffer()) return -1;
		
		return 0;
	}
	
}
