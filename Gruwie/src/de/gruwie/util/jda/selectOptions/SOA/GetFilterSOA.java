package de.gruwie.util.jda.selectOptions.SOA;

import de.gruwie.music.MusicController;
import de.gruwie.music.util.CheckVoiceState;
import de.gruwie.util.jda.selectOptions.types.SelectOptionAction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetFilterSOA extends SelectOptionAction {

	private final String name;
	private final TextChannel channel;
	private final Member member;
	
	public GetFilterSOA(String name, Member member, TextChannel channel) {
		super(name);
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
