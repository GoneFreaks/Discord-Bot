package de.gruwie.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.util.dto.CheckTrackDTO;

public class CheckTrack {

	private static void checkSubstring(List<CheckTrackDTO> storage, String input_query) {

		for (CheckTrackDTO i : storage) {
			String temp = i.getTitle();
			
			for (int j = 0; j < input_query.length(); j++) {
				if(temp.contains(input_query.substring(0, j+1))) i.addTreffer();
			}
		}
	}
	
	private static List<CheckTrackDTO> compareStrings(List<AudioTrack> input_list, String input_query) {
		
		List<CheckTrackDTO> storage = new ArrayList<>();
		for (int i = 0; i < input_list.size(); i++) {
			storage.add(new CheckTrackDTO(input_list.get(i)));
		}
		
		if(input_query.contains(" ")) {
			String[] input_arr = input_query.split(" ");
			
			for (int i = 0; i < input_arr.length; i++) {
				checkSubstring(storage, input_arr[i]);
			}
		}
		else {
			checkSubstring(storage, input_query);
		}
		return storage;
	}
	
	public static List<CheckTrackDTO> getAudioTrack (List<AudioTrack> queuelist, String query) {
		List<CheckTrackDTO> storage = compareStrings(queuelist, query.toLowerCase());
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
	
}
