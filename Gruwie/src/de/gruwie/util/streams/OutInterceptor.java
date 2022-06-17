package de.gruwie.util.streams;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import de.gruwie.util.GruwieUtilities;

public class OutInterceptor extends PrintStream {

	private static List<String> storage = new LinkedList<>();
	
	public OutInterceptor (PrintStream output) {
		super(output);
	}
	
	@Override
	public void println(String s) {
		super.println(s);
		if(s.equals("=")) storage.add(GruwieUtilities.getBorder(35, "="));
		else storage.add(s + "\n");
	}
	
	public static String getCmdOutput() {
		StringBuilder b = new StringBuilder("");
		storage.forEach((k) -> {
			b.append("\t\t" + k);
		});
		return b.toString();
	}
}
