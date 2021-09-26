package de.gruwie.util;

public class Formatter {

	public static String formatTime(long time) {
		long milli = time;
		long hours = time / 3600000;
		time -= hours * 3600000;
		long minutes = time / 60000;
		time -= minutes * 60000;
		long seconds = time / 1000;
		
		String h = hours + ":";
		String m = ((minutes < 10 && hours > 0)? "0" + minutes : minutes) + ":";
		String s = ((seconds < 10 && minutes > 0)? "0" + seconds : seconds) + "";
		return (hours > 0? h : "") + (minutes > 0? m : "") + (seconds > 0? s : (milli + "ms"));
	}
	
}
