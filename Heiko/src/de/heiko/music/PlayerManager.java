package de.heiko.music;

import java.util.concurrent.ConcurrentHashMap;

import de.heiko.Heiko_Startup;

public class PlayerManager {

	public ConcurrentHashMap<Long, MusicController> controller;
	
	public PlayerManager() {
		this.controller = new ConcurrentHashMap<Long, MusicController>();
	}
	
	public MusicController getController(long guildId) {
		MusicController mc = null;
		
		if(controller.containsKey(guildId)) mc = this.controller.get(guildId);
		else {
			mc = new MusicController(Heiko_Startup.INSTANCE.getShardMan().getGuildById(guildId));
			this.controller.put(guildId, mc);
		}
		return mc;
	}
	
}
