package de.gruwie.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.gruwie.music.AudioLoadResultBulk;
import de.gruwie.music.MusicController;
import de.gruwie.util.dto.AudioTrackTimed;
import de.gruwie.util.dto.TrackDTO;

public class TrackLoadingThread implements Runnable{

	private final List<TrackDTO> list;
	private final AudioPlayerManager apm;
	private final MusicController controller;
	private CountDownLatch latch;
	private List<AudioTrackTimed> tracks;
	
	public TrackLoadingThread (List<TrackDTO> list, AudioPlayerManager apm, MusicController controller) {
		this.list = list;
		this.apm = apm;
		this.controller = controller;
		this.latch = new CountDownLatch(list.size());
		this.tracks = new LinkedList<>();
	}
	
	@Override
	public void run() {
		for (TrackDTO i : list) {
			apm.loadItem(i.getUrl(), new AudioLoadResultBulk(controller, i, tracks, latch));
		}
		try {
			latch.await();
			controller.getQueue().addPlaylistToQueue(tracks);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
