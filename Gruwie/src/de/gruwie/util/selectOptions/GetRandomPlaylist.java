package de.gruwie.util.selectOptions;

import java.util.UUID;

import de.gruwie.db.PlaylistManager;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.util.ConfigManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class GetRandomPlaylist extends SelectOption implements SelectOptionAction {

	private final Member member;
	private final TextChannel channel;
	
	public GetRandomPlaylist (UUID value, Member member, TextChannel channel) {
		super("Random", value.toString());
		this.member = member;
		this.channel = channel;
	}
	
	@Override
	public void perform() {
		try {
			PlaylistManager.playPlaylist(member, channel, PlaylistDA.readRandom(ConfigManager.getInteger("random_count")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
