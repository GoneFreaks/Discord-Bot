package de.gruwie.util.dto;

public class TrackDTO {

	private final String url;
	private final long start;
	private final long end;
	
	public TrackDTO(String url, long start, long end) {
		this.url = url;
		this.start = start;
		this.end = end;
	}

	public String getUrl() {
		return url;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		return url;
	}
}
