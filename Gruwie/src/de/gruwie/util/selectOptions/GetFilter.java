package de.gruwie.util.selectOptions;

import java.util.UUID;

import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class GetFilter extends SelectOption implements SelectOptionAction {

	private final String name;
	private final TextChannel channel;
	private final Member member;
	
	public GetFilter(String name, UUID value, Member member, TextChannel channel) {
		super(name, value.toString());
		this.name = name;
		this.channel = channel;
		this.member = member;
	}

	@Override
	public void perform() {
		MusicController controller;
		try {
			controller = CheckVoiceState.checkVoiceState(member, channel);
			controller.getFilterManager().applyFilter(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
