package de.gruwie.music;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.util.GruwieIO;
import de.gruwie.util.dto.FilterDTO;

public class FilterManager {

	private String current_filter;
	
	private static ConcurrentHashMap<String, FilterDTO> default_filter;
	private static ConcurrentHashMap<String, FilterDTO> custom_filter;
	private MusicController controller;
	
	public FilterManager (MusicController controller) {
		this.current_filter = "DEFAULT";
		this.controller = controller;
		default_filter = new ConcurrentHashMap<>();
		List<FilterDTO> filters = new ArrayList<>();
		
		filters.add(new FilterDTO("DEFAULT", 0.0));
		filters.add(new FilterDTO("BASS", 0.15, 0.1, 0.05, 0.0, 0.0, -0.05, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1));
		filters.add(new FilterDTO("EARRAPE", 10, 10, 10, 10, 10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10));
		
		for (FilterDTO i : filters) {
			default_filter.put(i.getName(), i);
		}
		
		loadCustomFilters();
	}
	
	public static void loadCustomFilters() {
		Properties props = GruwieIO.loadProperties("filter.properties");
		if(props != null) {
			custom_filter = new ConcurrentHashMap<>();
			props.forEach((k,v) -> {
				default_filter.forEach((sk, sv) -> {
					if(sv.equals(k)) {
						System.err.println("The Filter: " + k + " cannot be loaded\n\t--> The name has already been used");
						return;
					}
				});
				String[] args = v.toString().trim().split(" ");
				if(args.length == 15) {
					try {
						double[] result = new double[15];
						for (int i = 0; i < args.length; i++) {
							double value = Double.parseDouble(args[i]);
							result[i] = value;
						}
						FilterDTO filter = new FilterDTO(k.toString(), result);
						custom_filter.put(filter.getName(), filter);
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
		List<FilterDTO> result = new ArrayList<>();
		result.addAll(default_filter.values());
		result.addAll(custom_filter.values());
		return result;
	}
	
	public void applyFilter(String name) throws Exception {
		FilterDTO temp = default_filter.get(name.toUpperCase());
		if(temp == null) temp = custom_filter.get(name.toUpperCase());
		if(temp != null) {
			current_filter = temp.getName();
			controller.getEqualizer().changeFreq(temp.getFilter());
		}
	}
	
	public static int filterCount() {
		if(default_filter != null && custom_filter != null) return default_filter.size() + custom_filter.size();
		else return -1;
	}
	
	public static int customFilterCount() {
		if(custom_filter != null) return custom_filter.size();
		else return -1;
	}
	
}
