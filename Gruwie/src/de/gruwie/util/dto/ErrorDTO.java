package de.gruwie.util.dto;

import java.io.PrintWriter;
import java.io.StringWriter;

import de.gruwie.util.Formatter;

public class ErrorDTO implements Comparable<ErrorDTO>{

	private String timestamp;
	private Exception ex;
	private String message;
	private String author;
	private long current_time;
	private String comment;
	private String guildId;
	
	public ErrorDTO(Exception ex, String message, String author, String guildId) {
		this.timestamp = Formatter.getDate() + " " + Formatter.getTime();
		this.current_time = System.currentTimeMillis();
		this.ex = ex;
		this.message = message;
		this.author = author;
		this.comment = null;
		this.guildId = guildId;
	}
	
	public ErrorDTO(Exception ex, String message, String author, String guildId, String comment){
		this(ex, message, author, guildId);
		this.comment = comment;
	}
	
	public Exception getException() {
		return this.ex;
	}
	
	public long getCurrent_Time () {
		return this.current_time;
	}
	
	@Override
	public String toString() {
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		
		return guildId + " " + timestamp + " " + message + " " + author + (comment == null? " " : (" " + comment + " ")) + sw.toString();
	}

	@Override
	public int compareTo(ErrorDTO o) {
		return (this.current_time < o.getCurrent_Time())? 1 : 0;
	}
	
}
