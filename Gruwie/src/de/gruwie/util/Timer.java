package de.gruwie.util;

import java.util.concurrent.TimeUnit;

public class Timer {
	
	public static void delay (TimeUnit unit, int value) {
		try {
			unit.sleep(value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
