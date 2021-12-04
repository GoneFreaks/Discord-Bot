package de.gruwie.util;

import java.io.OutputStream;
import java.io.PrintStream;

public class Filter {
	public static  void filter() { System.setErr(new Interceptor(System.err));}
}

class Interceptor extends PrintStream {
	
	public Interceptor(OutputStream out) {
		super(out, true);
	}
	
	@Override
	public void print(String s) {
		if(!s.contains("lava")) super.print(s);
	}
	
	@Override
	public void println(String s) {
		if(!s.contains("lava")) {
			if(s.contains("INFO")) System.out.println(s);
			else super.println(s);
		}
	}
	
}
