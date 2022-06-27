package de.gruwie.util.streams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import de.gruwie.util.GruwieUtilities;

/**
 * Redirect the <code>System.err</code> stream to the given file.</br>
 * Each exception will be stored in a Collection for later usage for the <code>Metadata-Command</code>
 */
public class ErrInterceptor extends PrintStream {

	public static int counter = 0;
	public static List<String> exceptions = new LinkedList<>();
	
	public ErrInterceptor(File file) throws FileNotFoundException {
		super(file);
	}
	
	@Override
	public void println(Object o) {
		if(o instanceof Exception) {
			super.println("--------------------------------------------------------------------------------------------------------------------------------\n"
					+ GruwieUtilities.getTime(System.currentTimeMillis()));
			exceptions.add(GruwieUtilities.getTime(System.currentTimeMillis()) + ": " + ((Exception) o).getMessage());
			counter++;
		}
		super.println("\t\t" + o);
		System.out.println(o);
	}
	
	@Override
	public void println(String o) {
		if(!o.startsWith("<")) o = "<" + GruwieUtilities.getTime(System.currentTimeMillis()) + ">\t" + o;
		super.println(o);
		System.out.println(o);
	}
}
