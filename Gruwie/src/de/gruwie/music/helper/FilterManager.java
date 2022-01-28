package de.gruwie.music.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.music.MusicController;
import de.gruwie.util.dto.FilterDTO;

public class FilterManager {

	private String current_filter;
	
	private List<FilterDTO> filters;
	private ConcurrentHashMap<String, FilterDTO> storage;
	private MusicController controller;
	
	public FilterManager (MusicController controller) {
		this.current_filter = "DEFAULT";
		this.controller = controller;
		this.storage = new ConcurrentHashMap<>();
		this.filters = new ArrayList<>();
		
		filters.add(new FilterDTO("DEFAULT", 0.0));
		filters.add(new FilterDTO("BASS", 0.15, 0.1, 0.05, 0.0, 0.0, -0.05, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1));
		filters.add(new FilterDTO("EARRAPE", 10, 10, 10, 10, 10, -10, -10, -10, -10, -10, -10, -010, -10, -10, -10));
		
		for (FilterDTO i : filters) {
			storage.put(i.getName(), i);
		}
	}
	
	public String getCurrentFilter() {
		return current_filter;
	}
	
	public List<FilterDTO> getFilter(){
		return filters;
	}
	
	public void applyFilter(String name) throws Exception {
		FilterDTO temp = storage.get(name.toUpperCase());
		if(temp != null) {
			current_filter = temp.getName();
			controller.getEqualizer().changeFreq(temp.getFilter());
			controller.getQueue().editQueueMessage();
		}
	}
	
}
