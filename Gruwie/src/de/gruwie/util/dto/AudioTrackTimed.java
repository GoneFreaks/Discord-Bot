package de.gruwie.util.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.TrackMarker;
import com.sedmelluq.discord.lavaplayer.track.TrackMarkerHandler.MarkerState;

import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.Threadpool;

public class AudioTrackTimed {

	private final AudioTrack track;
	private final TrackMarker end_marker;
	private final long start_position;
	private final long end;
	private AudioTrack timed_track;
	
	public AudioTrackTimed(AudioTrack track, long start, long end) {
		this.track = track;
		this.start_position = start;
		this.end_marker = new TrackMarker(end, (state) -> {
			if(state == MarkerState.REACHED) {
				System.out.println(state.toString());
			}
		});
		this.end = end;
		this.timed_track = track.makeClone();
		if(end != track.getDuration()) timed_track.setMarker(end_marker);
		timed_track.setPosition(start_position);
	}
	
	public AudioTrack getTrack() {
		return track;
	}
	
	public AudioTrack getTrackClone() {
		Threadpool.execute(() -> {
			GruwieUtilities.delay(TimeUnit.SECONDS, 1);
			this.timed_track = track.makeClone();
			if(end != track.getDuration()) timed_track.setMarker(end_marker);
			timed_track.setPosition(start_position);
		});
		return timed_track;
	}
	
	public String getTitle() {
		return track.getInfo().title;
	}
	
	public long getLength() {
		return track.getInfo().length;
	}
	
	public static AudioTrackTimed convertToTimed(AudioTrack track) {
		return new AudioTrackTimed(track, 0, track.getDuration());
	}
	
	public static List<AudioTrackTimed> convertToTimed(List<AudioTrack> tracks){
		List<AudioTrackTimed> result = new ArrayList<>();
		tracks.forEach((k) -> {
			result.add(new AudioTrackTimed(k, 0, k.getDuration()));
		});
		return result;
	}
	
	public static List<AudioTrack> convertToNonTimed(List<AudioTrackTimed> tracks){
		List<AudioTrack> result = new ArrayList<>();
		tracks.forEach((k) -> {
			result.add(k.getTrack());
		});
		return result;
	}
	
	@Override
	public String toString() {
		return track.getInfo().title;
	}
	
}
