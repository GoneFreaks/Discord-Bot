package de.gruwie.util.dto;

import java.util.List;

public class PlaylistsDTO {

	private List<String> guild_playlists;
	private List<String> user_playlists;
	
	public PlaylistsDTO(List<String> guild_playlists, List<String> user_playlists) {
		this.guild_playlists = guild_playlists;
		this.user_playlists = user_playlists;
	}

	public List<String> getGuild_playlists() {
		return guild_playlists;
	}

	public List<String> getUser_playlists() {
		return user_playlists;
	}
	
	public int size() {
		return guild_playlists.size() + user_playlists.size();
	}
}
