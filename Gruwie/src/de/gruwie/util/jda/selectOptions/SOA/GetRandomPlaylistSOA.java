package de.gruwie.util.jda.selectOptions.SOA;

import de.gruwie.db.PlaylistManager;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.jda.selectOptions.types.SelectOptionAction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetRandomPlaylistSOA extends SelectOptionAction {

	private final Member member;
	private final TextChannel channel;
	
	public GetRandomPlaylistSOA (Member member, TextChannel channel) {
		super("Random");
		this.member = member;
		this.channel = channel;
	}
	
	@Override
	public void perform() {
		try {
			PlaylistManager.playPlaylist(member, channel, PlaylistDA.readRandom(ConfigManager.getInteger("random_count")), "Random[" + ConfigManager.getInteger("random_count") +  "]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
