package de.gruwie.util.jda;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.Message;

public class MessageHolder {

	private static ConcurrentHashMap<Message, Long> storage = new ConcurrentHashMap<>();
	
	public static void start() {
		Thread checker = new Thread(() -> {
			while(true) {
				try {
					TimeUnit.SECONDS.sleep(60);
					final long current = System.currentTimeMillis();
					storage.forEach((k,v) -> {
						long difference = current - v;
						if(!k.isPinned() && difference > (60000 * ConfigManager.getInteger("delete_other_time"))) {
							k.delete().queue(null, Filter.handler);
							storage.remove(k);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		checker.setDaemon(true);
		checker.start();
	}
	
	public static void add(Message m) {
		storage.put(m, System.currentTimeMillis());
	}
	
	public static void shutdown() {
		storage.forEach((k,v) -> {
			if(!k.isPinned()) k.delete().queue(null, Filter.handler);
			storage.remove(k);
		});
	}
	
	public static void checkMessage(String id) {
		storage.forEach((k,v) -> {
			if(id.equals(k.getId())) storage.remove(k);
		});
	}
	
}
