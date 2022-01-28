package de.gruwie.util;

import java.io.File;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Interceptor extends PrintStream {

	private final SimpleDateFormat pattern;
	private final File file;
	
	public Interceptor(File file) throws Exception {
		super(file);
		this.file = file;
		pattern = new SimpleDateFormat("HH:mm:ss");
	}
	
	@Override
	public void println(Object o) {
		if(o instanceof Exception) {
			super.println("--------------------------------\n" + getCurrentTime());
			System.out.println("An Exception occured the log-file at " + file.getAbsolutePath() + " will provide more information");
		}
		super.println("\t\t" + o);
	}
	
	private String getCurrentTime() {
		return pattern.format(new Timestamp(System.currentTimeMillis()));
	}
}
