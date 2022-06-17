package de.gruwie.util.selectOptions;

import java.util.UUID;

import de.gruwie.db.da.TrackDA;
import de.gruwie.util.SelectionMenuManager;
import net.dv8tion.jda.api.interactions.components.Button;

public class DeleteTrackBA implements ButtonAction{

	private final boolean delete;
	private final Button button;
	private final UUID uuid;
	private final String url;
	
	public DeleteTrackBA(boolean delete, String url) {
		this.delete = delete;
		this.url = url;
		this.uuid = SelectionMenuManager.getUUID();
		if(delete) this.button = Button.danger(uuid.toString(), "DELETE");
		else this.button = Button.success(uuid.toString(), "CANCEL");
		SelectionMenuManager.putButtonAction(this, uuid);
	}
	
	@Override
	public void perform() {
		if(delete) TrackDA.deleteCertainTrack(url);
	}

	@Override
	public Button getButton() {
		return button;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}
}
