package de.gruwie.util.dto;

import java.util.List;

public class InsertTrackDTO {

	private List<Integer> available;
	private List<String> unavailable;
	
	public InsertTrackDTO(List<Integer> available, List<String> unavailable) {
		this.available = available;
		this.unavailable = unavailable;
	}

	public List<Integer> getAvailable() {
		return available;
	}

	public List<String> getUnavailable() {
		return unavailable;
	}
}
