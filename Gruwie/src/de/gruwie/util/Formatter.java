package de.gruwie.util;

import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
	
	public static String getTime() {
		return format("HH:mm:ss");
	}
	
	public static String getDate() {
		return format("yyyy-MM-dd");
	}
	
	public static void printBorderline (String element) {
		for (int i = 0; i < 50; i++) {
			System.out.print((i < 49? element : "\n"));
		}
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
		String command = input.replaceAll("Command", "").replaceAll("Lobby", "");
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
	
}
