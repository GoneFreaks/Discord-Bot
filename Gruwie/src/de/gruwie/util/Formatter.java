package de.gruwie.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	public static String getTime() {
		return format("HH:mm:ss");
	}
	
	public static String getDate() {
		return format("yyyy-MM-dd");
	}
	
	private static String format(String pattern) {
		SimpleDateFormat f = new SimpleDateFormat(pattern);
		Date d = new Date();
		return f.format(d);
	}
	
	private static String[] getInfoFromTitle (String title) throws Exception {
		
		if(!title.contains("-")) return null;
		
		String[] temp = title.split("-");
		if(temp.length != 2) return null;
		
		
		String[] result = new String[2];
		for (int i = 0; i < temp.length; i++) {
			String element = temp[i].replaceAll(" ", "");
			if(!(element.contains("(") || element.contains("["))) {
				result[i] = element;
			}
			else {
				
				if(element.contains("(")){
					int index = element.lastIndexOf("(");
					element = element.substring(0, index);
					result[i] = element;
				}
				if(element.contains("[")) {
					int index = element.lastIndexOf("[");
					element = element.substring(0, index);
					result[i] = element;
				}
			}
		}
		return result;
	}
	
	public static String[] getURL(String title) throws Exception {
		String[] title_author = getInfoFromTitle(title);
		String[] result = new String[2];
		
		if(title_author == null) return null;
		
		result[0] = ("https://www.azlyrics.com/lyrics/" + title_author[0] + "/" + title_author[1] + ".html").toLowerCase();
		result[1] = ("https://www.azlyrics.com/lyrics/" + title_author[1] + "/" + title_author[0] + ".html").toLowerCase();
		
		return result;
	}
	
	public static String formatWebsite(String input) {
		if(input.equals("")) return input;
		
		int index1 = 0;
		
		for (int i = 0; i < input.length(); i++) {
			if(input.charAt(i) == '.' && input.charAt(i+1) == ' ' && input.charAt(i+2) == '-' && input.charAt(i+3) == '-' && input.charAt(i+4) == '>') {
				index1 = i+5;
				break;
			}
		}
		
		String temp = input.substring(index1);
		int index = 0;
		for (int i = 0; i < temp.length(); i++) {
			if(temp.charAt(i) == '/' && temp.charAt(i+1) == 'd' && temp.charAt(i+2) == 'i' && temp.charAt(i+3) == 'v') {
				index = i;
				break;
			}
		}
		
		String temp2 = temp.substring(0, index - 1);
		String temp3 = temp2.replaceAll("<br>", "\n");
		String temp4 = temp3.replaceAll("<i>", "");
		String temp5 = temp4.replaceAll("</i>", "");
		String ready = temp5;
		
		return ready;
	}
	
}
