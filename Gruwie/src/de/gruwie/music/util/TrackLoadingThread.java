package de.gruwie.music.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.music.AudioLoadResultBulk;
import de.gruwie.music.MusicController;

public class TrackLoadingThread implements Runnable{

	private final List<String> list;
	private final AudioPlayerManager apm;
	private final MusicController controller;
	private CountDownLatch latch;
	private List<AudioTrack> tracks;
	
	public TrackLoadingThread (List<String> list, AudioPlayerManager apm, MusicController controller) {
		this.list = list;
		this.apm = apm;
		this.controller = controller;
		this.latch = new CountDownLatch(list.size());
		this.tracks = new LinkedList<>();
	}
	
	@Override
	public void run() {
		for (String i : list) {
			apm.loadItem(i, new AudioLoadResultBulk(controller, i, tracks, latch));
		}
		try {
			latch.await();
			controller.getQueue().addPlaylistToQueue(tracks);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
