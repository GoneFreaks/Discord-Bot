package de.gruwie.util;

import java.io.File;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class ErrInterceptor extends PrintStream {

	public static int counter = 0;
	public static List<String> exceptions = new LinkedList<>();
	
	public ErrInterceptor(File file) throws Exception {
		super(file);
	}
	
	@Override
	public void println(Object o) {
		if(o instanceof Exception) {
			super.println("--------------------------------------------------------------------------------------------------------------------------------\n"
					+ Formatter.getTime(System.currentTimeMillis()));
			Exception e = (Exception) o;
			System.out.println("[" + e.getClass() + "]");
			exceptions.add(Formatter.getTime(System.currentTimeMillis()) + ": " + ((Exception) o).getMessage());
			counter++;
		}
		super.println("\t\t" + o);
	}
}
