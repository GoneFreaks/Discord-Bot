package de.gruwie.util;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.util.selectOptions.SelectOptionAction;

public class SelectionMenuManager {

	private static ConcurrentHashMap<UUID, SelectOptionAction> storage = new ConcurrentHashMap<>();
	private static Set<UUID> taken = new HashSet<>();
	
	public static UUID getUUID () {
		UUID result;
		while(true) {
			result = UUID.randomUUID();
			if(!storage.contains(result)) break;
		}
		taken.add(result);
		return result;
	}
	
	public static void putAction (UUID uuid, SelectOptionAction action) {
		storage.put(uuid, action);
		taken.remove(uuid);
	}
	
	public static void executeAction (String uuid) {
		SelectOptionAction action = storage.remove(UUID.fromString(uuid));
		if(action != null) action.perform();
		else System.err.println("UNKNOW ACCESS");
	}
	
}
