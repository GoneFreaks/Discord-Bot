package de.gruwie.util.dto;

public class FilterDTO {

	private String name;
	private float[] filter;
	
	public FilterDTO (String name, double ... input) {
		this.name = name;
		this.filter = new float[15];
		for (int i = 0; i < input.length; i++) {
			filter[i] = (float) input[i];
		}
		if(input.length != 15) {
			for (int i = 0; i < 15 - input.length; i++) {
				filter[i + input.length] = 0;
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public float[] getFilter() {
		return filter;
	}
	
	@Override
	public boolean equals(Object other) {
		return other.toString().equalsIgnoreCase(this.getName());
	}
	
}
