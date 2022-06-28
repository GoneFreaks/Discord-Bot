package de.gruwie.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.attribute.FileTime;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import de.gruwie.util.dto.CommandDTO;

public class GruwieUtilities {

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
		if(hours > 99) return "> 99h";
		return (hours > 0? h : "") + (minutes > 0? m : (hours > 0? "00:" : "0:")) + s;
	}
	
	private static String[] getInfoFromTitle (String title) throws Exception {
		log();
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
		log();
		String[] title_author = getInfoFromTitle(title);
		String[] result = new String[2];
		
		if(title_author == null) return null;
		
		result[0] = ("https://www.azlyrics.com/lyrics/" + title_author[0] + "/" + title_author[1] + ".html").toLowerCase();
		result[1] = ("https://www.azlyrics.com/lyrics/" + title_author[1] + "/" + title_author[0] + ".html").toLowerCase();
		
		return result;
	}
	
	public static String formatWebsite(String input) {
		log();
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
	
	public static String getBorder(int number, String symbol) {
		StringBuilder b = new StringBuilder("");
		for (int i = 0; i < number; i++) {
			b.append(symbol);
		}
		b.append("\n");
		return b.toString();
	}
	
	public static void delay (TimeUnit unit, int value) {
		try {
			unit.sleep(value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static InputStream getInputStream (String link) {
		log();
		try {
			URL url = new URL(link);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(3000);
			con.setReadTimeout(3000);
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if(responseCode == 200) {
				return con.getInputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String doWebBrowsing(String input, String break_string) throws Exception {
		log();
		StringBuilder b = new StringBuilder("");
		InputStream website = getInputStream(input);
		if(website == null) return "";
		try (Scanner in = new Scanner(website)){
			while (in.hasNext()) {
				String line = in.nextLine();
				b.append(line);
				if(line.contains(break_string)) break;
			}
				
		}
		String edit = b.toString().replace(';', '\n');
		return edit;
	}
	
	public static Properties loadProperties(String file) {
		log();
		try {
			Properties result = new Properties();
			File temp = new File(file);
			InputStream in = new FileInputStream(temp);
			result.load(in);
			in.close();
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void log(String message) {
		StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
		String calling_class = walker.getCallerClass().getName();
		String calling_method = walker.walk(frames -> frames.map(StackWalker.StackFrame::getMethodName).skip(1).findFirst()).toString().replace("Optional[", "").replace("]", "");
		
		System.err.println("<" + getTime(System.currentTimeMillis()) + ">\tCLASS: " + calling_class + " METHOD: " + calling_method + "\t" + message);
	}
	
	public static void log() {
		StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
		String calling_class = walker.getCallerClass().getName();
		String calling_method = walker.walk(frames -> frames.map(StackWalker.StackFrame::getMethodName).skip(1).findFirst()).toString().replace("Optional[", "").replace("]", "");
		
		System.err.println("<" + getTime(System.currentTimeMillis()) + ">\tCLASS: " + calling_class + " METHOD: " + calling_method + "\tENTER");
	}
	
	public static void logMeta(String message) {
		System.err.println("<" + getTime(System.currentTimeMillis()) + ">\t" + message);
	}
 	
}
