package de.gruwie.util;

import java.nio.file.attribute.FileTime;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.gruwie.util.dto.CommandDTO;

public class Formatter {

	private static List<String> shortcuts = new ArrayList<>();
	
	public static String formatTime(long time) {
		long hours = time / 3600000;
		time -= hours * 3600000;
		long minutes = time / 60000;
		time -= minutes * 60000;
		long seconds = time / 1000;
		
		String h = hours + ":";
		String m = ((minutes < 10 && hours > 0)? "0" + minutes : minutes) + ":";
		String s = ((seconds < 10)? "0" + seconds : seconds) + "";
		return (hours > 0? h : "") + (minutes > 0? m : (hours > 0? "00:" : "0:")) + s;
	}
	
	public static void printBorderline (String element) {
		for (int i = 0; i < 50; i++) {
			System.out.print((i < 49? element : "\n"));
		}
	}
	
	private static String[] getInfoFromTitle (String title) throws Exception {
		
		if(!title.contains("-")) return null;
		
		String[] temp = title.split("-");
		if(temp.length != 2) return null;
		
		
		String[] result = new String[2];
		for (int i = 0; i < temp.length; i++) {
			String element = temp[i].replaceAll(" ", "");
			if((element.contains("(") || element.contains("["))) {
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
			else result[i] = element;
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
	
	public static CommandDTO createNames(String input, boolean tryShortcut) {
		String command = input.replaceAll("Command", "");
		StringBuilder b = new StringBuilder("");
		for (int i = 0; i < command.length(); i++) {
			if(command.charAt(i) > 'A' && command.charAt(i) < 'Z') b.append(command.charAt(i));
		}
		String shortcut = b.toString().toLowerCase();
		if(tryShortcut) shortcuts.add(shortcut);
		else shortcut = null;
		
		return new CommandDTO(command.toLowerCase(), shortcut);
	}
	
	public static String formatFileTime(FileTime fileTime) {
		LocalDateTime localDateTime = fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
	}
	
	private static final SimpleDateFormat TIME_PATTERN = new SimpleDateFormat("HH:mm:ss");
	public static String getTime(long input) {
		return TIME_PATTERN.format(new Timestamp(input));
	}
	
	private static final SimpleDateFormat DATETIME_PATTERN = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static String getDateTime(long input) {
		return DATETIME_PATTERN.format(new Timestamp(input));
	}
	
	public static String formatByteSize(long size) {
		double temp = size;
		int counter = 0;
		while(temp / 1024 >= 1) {
			temp /= 1024.0;
			counter++;
		}
		
		String result = formatDouble(temp);
		
		switch (counter) {
		case 1:
			result += "kb";
			break;
		case 2:
			result += "Mb";
			break;
		case 3:
			result += "Gb";
			break;
		case 4:
			result += "Tb";
			break;
		}
		
		return result;
	}
	
	public static String formatDouble(double input) {
		String asString = input + "";
		if(!asString.contains(".")) return asString;
		else {
			int index = asString.lastIndexOf(".") + 3;
			if(index > asString.length()) return asString;
			else return asString.substring(0, index);
		}
	}
	
}
