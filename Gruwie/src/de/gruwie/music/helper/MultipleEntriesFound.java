package de.gruwie.music.helper;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.util.SelectionMenuManager;
import de.gruwie.util.dto.CheckTrackDTO;
import de.gruwie.util.selectOptions.SelectOptionAction;
import de.gruwie.util.selectOptions.SetOrRemoveTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class MultipleEntriesFound {

	public static void promptDialog (String message, List<CheckTrackDTO> track_list, TextChannel channel, Member member, boolean isSetter) {
		
		if(track_list.size() > 5) {
			return;
		}
		
		StringBuilder c = new StringBuilder("**Multiple entries have been found:**\n\n*");
		
		for(int i = 0; i < track_list.size(); i++) {
			c.append((i+1) + ": " + track_list.get(i).getTitleOriginal() + (i+1 == track_list.size()? "*" : "\n"));
		}
		
		c.append("\n\n**Which track should be deleted?**");
		
		List<SelectOptionAction> actions = new ArrayList<>();
		track_list.forEach((k) -> {
			actions.add(new SetOrRemoveTrack(k.getTitleOriginal(), member, channel, isSetter));
		});
		SelectionMenuManager.createDropdownMenu(actions, channel, c.toString());
	}
	
}
