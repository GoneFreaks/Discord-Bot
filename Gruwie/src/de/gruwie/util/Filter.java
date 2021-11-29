package de.gruwie.util;

import java.io.OutputStream;
import java.io.PrintStream;

public class Filter {
	
	public static void filter() {
		PrintStream interceptor = new Interceptor(System.err);
		System.setErr(interceptor);
	}
}

class Interceptor extends PrintStream {
	
	public Interceptor(OutputStream out) {
		super(out, true);
	}
	
	@Override
	public void print(String s) {
		if(s.contains("403") || s.contains("sedmelluq") || s.contains("at java.base") || s.contains("more"));
		else super.print(s);
	}
}
