package de.gruwie.util.jda;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import de.gruwie.util.ConfigManager;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.Message;

public class MessageHolder {

	private static ConcurrentHashMap<Message, Long> delete_after_time = new ConcurrentHashMap<>();
	private static Set<Message> delete_on_shutdown = Collections.newSetFromMap(new ConcurrentHashMap<Message, Boolean>());
	
	public static void start() {
		Thread checker = new Thread(() -> {
			while(true) {
				try {
					TimeUnit.SECONDS.sleep(60);
					final long current = System.currentTimeMillis();
					delete_after_time.forEach((k,v) -> {
						long difference = current - v;
						System.out.println(difference);
						if(difference > (60000 * ConfigManager.getInteger("delete_other_time"))) {
							k.delete().queue(null, Filter.handler);
							delete_after_time.remove(k);
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
	
	public static void add(Message m, boolean delete) {
		if(delete) delete_after_time.put(m, System.currentTimeMillis());
		else delete_on_shutdown.add(m);
	}
	
	public static void shutdown() {
		delete_after_time.forEach((k,v) -> {
			k.delete().queue(null, Filter.handler);
			delete_after_time.remove(k);
		});
		delete_on_shutdown.forEach((k) -> {
			k.delete().queue(null, Filter.handler);
			delete_on_shutdown.remove(k);
		});
	}
	
}
