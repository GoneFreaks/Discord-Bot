package de.gruwie.music;

import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.Gruwie_Startup;

public class PlayerManager {

	private ConcurrentHashMap<Long, MusicController> controller;
	
	public PlayerManager() {
		this.controller = new ConcurrentHashMap<Long, MusicController>();
	}
	
	public MusicController getController(long guildId) {
		MusicController mc = null;
		
		if(this.controller.containsKey(guildId)) mc = this.controller.get(guildId);
		else {
			mc = new MusicController(Gruwie_Startup.INSTANCE.getShardMan().getGuildById(guildId));
			this.controller.put(guildId, mc);
		}
		return mc;
	}
	
	public long getGuildByPlayerHash(int hash) {
		
		for(MusicController controller: this.controller.values()) {
			if(controller.getPlayer().hashCode() == hash) {
				return controller.getGuild().getIdLong();
			}
		}
		return -1;
	}
}
