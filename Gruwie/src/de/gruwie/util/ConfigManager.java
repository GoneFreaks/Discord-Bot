package de.gruwie.util;

import java.util.Properties;

public class ConfigManager {

	private static Properties config;
	
	public static boolean startup() {
		GruwieUtilities.log();
		boolean result = reload();
		if(result) {
			databaseActive = getBoolean("database");
			refreshTimer = getInteger("refresh");
		}
		return result;
	}
	
	public static boolean reload() {
		GruwieUtilities.log();
		config = GruwieUtilities.loadProperties("config.properties");
		if(config == null) return false;
		else return true;
	}
	
	public static String configToString() {
		GruwieUtilities.log();
		StringBuilder b = new StringBuilder("***0 --> false --> deactivated\n1 --> true --> activated\n\n***");
		for (Object i : config.keySet()) {
			b.append(i + "\n**" + config.getProperty(i.toString()) + "**\n\n");
		}
		return b.toString();
	}

	public static boolean getBoolean(String name) {
		GruwieUtilities.log();
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
		GruwieUtilities.log();
		try {
			return Double.parseDouble(config.getProperty("name"));
		} catch (Exception e) {
			return 0.5;
		}
	}
	
	private static boolean databaseActive;
	public static boolean getDatabase() {
		GruwieUtilities.log();
		return databaseActive;
	}
	
	private static int refreshTimer;
	public static int getRefreshTimer() {
		return refreshTimer;
	}
	
}
