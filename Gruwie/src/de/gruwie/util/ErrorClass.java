package de.gruwie.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErrorClass {

	private static List<ErrorDTO> storage = new ArrayList<>();
	
	public static void reportError(ErrorDTO err) {
		err.getException().printStackTrace();
		storage.add(err);
	}
	
	public static void shutdown() {
		
		long timestamp = System.currentTimeMillis();
		
		if(storage.size() > 0) {
			System.out.println("ErrorClass: Daten werden geschrieben");
			
			Collections.sort(storage);
			for (ErrorDTO i : storage) {
				GruwieIO.writeToFile(timestamp + ".txt", i.toString() + borderline());
			}
		}
	}
	
	private static String borderline() {
		StringBuilder b = new StringBuilder("\n");
		for (int i = 0; i < 50; i++) {
			b.append("===");
		}
		b.append("\n");
		return b.toString();
	}
	
}
