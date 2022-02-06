package de.gruwie.music.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.music.MusicController;
import de.gruwie.util.GruwieIO;
import de.gruwie.util.dto.FilterDTO;

public class FilterManager {

	private String current_filter;
	
	private List<FilterDTO> filters;
	private static ConcurrentHashMap<String, FilterDTO> storage;
	private static int default_filter_count;
	private MusicController controller;
	
	public FilterManager (MusicController controller) {
		this.current_filter = "DEFAULT";
		this.controller = controller;
		storage = new ConcurrentHashMap<>();
		this.filters = new ArrayList<>();
		
		filters.add(new FilterDTO("DEFAULT", 0.0));
		filters.add(new FilterDTO("BASS", 0.15, 0.1, 0.05, 0.0, 0.0, -0.05, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1));
		filters.add(new FilterDTO("EARRAPE", 10, 10, 10, 10, 10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10));
		
		default_filter_count = filters.size();
		
		loadCustomFilters();
		
		for (FilterDTO i : filters) {
			storage.put(i.getName(), i);
		}
	}
	
	private void loadCustomFilters() {
		Properties props = GruwieIO.loadProperties("filter.properties");
		if(props != null) {
			props.forEach((k,v) -> {
				for (FilterDTO i : filters) {
					if(i.equals(k)) {
						System.err.println("The Filter: " + k + " cannot be loaded\n\t--> The name has already been used");
						return;
					}
				}
				String[] args = v.toString().trim().split(" ");
				if(args.length == 15) {
					try {
						double[] result = new double[15];
						for (int i = 0; i < args.length; i++) {
							double value = Double.parseDouble(args[i]);
							result[i] = value;
						}
						filters.add(new FilterDTO(k.toString(), result));
					} catch (Exception e) {
						System.err.println("The Filter: [" + k + "] cannot be loaded\n\t--> Check Number-Format");
					}
				}
				else System.err.println("The Filter: " + k + " cannot be loaded\n\t--> You have to provide exactly 15 Numbers");
			});
		}
		else System.out.println("Unable to load Custom-Filter");
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
	
	public static int filterCount() {
		if(storage != null) return storage.size();
		else return -1;
	}
	
	public static int customFilterCount() {
		if(storage != null) return storage.size() - default_filter_count;
		else return -1;
	}
	
}
