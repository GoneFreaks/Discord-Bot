package de.gruwie.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

	private static Properties config;
	
	public static boolean startup() {
		config = new Properties();
		try {
			InputStream in = new FileInputStream(new File("config.properties"));
			config.load(in);
			in.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String configToString() {
		StringBuilder b = new StringBuilder("***0 --> false --> deactivated\n1 --> true --> activated\n\n***");
		for (Object i : config.keySet()) {
			b.append(i + "\n**" + config.getProperty(i.toString()) + "**\n\n");
		}
		return b.toString();
	}

	public static boolean getBoolean(String name) {
		
		String value = config.getProperty(name);
		if(value.equals("0") || value.equals("1")) {
			return value.equals("1");
		}
		else {
			return true;
		}
	}
	
	public static String getString(String name) {
		return config.getProperty(name);
	}
	
	public static int getInteger(String name) {
		try {
			return Integer.parseInt(config.getProperty(name));
		} catch (Exception e) {
			return 5;
		}
	}
	
	public static double getDouble(String name) {
		try {
			return Double.parseDouble(config.getProperty("name"));
		} catch (Exception e) {
			return 0.5;
		}
	}
	
}
