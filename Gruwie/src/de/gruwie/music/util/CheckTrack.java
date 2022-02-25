package de.gruwie.music.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.util.dto.CheckTrackDTO;

public class CheckTrack {
	
	public static List<CheckTrackDTO> getAudioTrack (List<AudioTrack> queuelist, String query) {
		
		if(queuelist.size() == 0) return null;
		
		List<CheckTrackDTO> storage = compare(queuelist, query.toLowerCase());
		Collections.sort(storage);
		
		if(storage.size() == 1) {
			return storage.subList(0, 1);
		}
		if(storage.size() > 1) {
			for (int i = 1; i < storage.size(); i++) {
				if(storage.get(i-1).getTreffer() != storage.get(i).getTreffer()) {
					return storage.subList(0, i);
				}
			}
			return storage.subList(0, storage.size());
		}
		return null;
	}

	private static List<CheckTrackDTO> compare(List<AudioTrack> queuelist, String query) {
		String[] args = query.split(" ");
		List<CheckTrackDTO> list = new LinkedList<>();
		queuelist.forEach((k) -> {
			list.add(new CheckTrackDTO(k));
		});
		list.forEach((k) -> {
			String title = k.getTitle();
			for (int i = 0; i < args.length; i++) {
				String current = args[i].toLowerCase();
				if(title.contains(current + " ") || title.contains(" " + current)) k.addTreffer();
				if(title.contains(current)) k.addTreffer();
			}
		});
		return reduceList(list);
	}
	
	private static List<CheckTrackDTO> reduceList(List<CheckTrackDTO> list) {
		Collections.sort(list);
		int max = list.get(0).getTreffer();
		return list.parallelStream().filter(i -> i.getTreffer() == max).collect(Collectors.toList());
	}
	
}
