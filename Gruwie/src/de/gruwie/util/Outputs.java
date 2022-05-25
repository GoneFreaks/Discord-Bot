package de.gruwie.util;

public enum Outputs {
	
	//###############################
	//###############################
	// REACITON-OUTPUTS
	//###############################
	//###############################
	
	VOICE_CHANNEL("**YOU HAVE TO BE IN A VOICE-CHANNEL**"),
	DATABASE("**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**"),
	PERMISSION("**YOU DONT HAVE THE PERMISSION TO USE THIS COMMAND**"),
	INVALID_PARAMETERS("**WRONG PATTERN, CHECK *HELP* FOR MORE DETAILS**"),
	DEFAULT_FOOTER("Executed by: Java " + System.getProperty("java.version") + "\nGitHub: https://github.com/GoneFreaks/Discord-Bot"),
	OUTPUT_CHANNEL_SET("**OUTPUT-CHANNEL HAS BEEN SET**" + (ConfigManager.getDatabase()? "" : " **TEMPORARILY**")),
	TO_BE_DONE("**NOT YET IMPLEMENTED**"),
	UNKNOWN_COMMAND("**I DON'T KNOW THIS COMMAND ¯\\_(\u30C4)_/¯**"),
	LYRICS("**Unable to find lyrics**"),
	AMBIGUOUS("**THE PROVIDED INPUT IS TOO AMBIGUOUS**"),
	LYRICS_FOOTER("Powered by: www.azlyrics.com"),
	NOTHING_FOUND("**NO ENTRIES FOUND**"),
	EXPORT_MISSING_NAME("**YOU HAVE TO PROVIDE A NAME FOR YOU'RE PLAYLIST**"),
	EXPORT_MAX_CHARACTERS("**YOU CAN ONLY USE UP TO 30-CHARACTERS PER PLAYLIST**"),
	EXPORT_EMPTY_QUEUE("**THE QUEUE IS EMPTY, NOTHING TO SAVE**"),

	
	//###############################
	//###############################
	// COMMAND-FIELDS
	//###############################
	//###############################
	
	PARAMETERS_QUERY("Search-Query, in order to identify a track in the queue"),
	
	//###############
	// Clear
	//###############
	DESCRIPTION_CLEAR("Delete messages in the current channel\n"
			+ "You need the permission to manage messages in order to use this command"),
	SHORT_DESCRIPTION_CLEAR("Delete messages"),
	OPTIONAL_PARAMETERS_CLEAR("**n** the number of messages to delete"),
	
	//###############
	// Set
	//###############
	DESCRIPTION_SET("Choose which channel Gruwie should use as the output-channel\n"
			+ "Gruwie will react to messages regardless of the channel they were sent in"),
	SHORT_DESCRIPTION_SET("Set Output-Channel"),
	
	//###############
	// Help
	//###############
	DESCRIPTION_HELP("A collection of all commands available\n"
			+ "Also the command to get help for other commands"),
	SHORT_DESCRIPTION_HELP("Help/Manual-Pages"),
	OPTIONAL_PARAMETERS_HELP("A command or its shortcut"),
	
	
	//###############################
	//###############################
	// MUSIC-COMMAND-FIELDS
	//###############################
	//###############################
	
	//###############
	// ClearQueue
	//###############
	DESCRIPTION_CLEARQUEUE("Clear the music-queue"),
	SHORT_DESCRIPTION_CLEARQUEUE("Clear current queue"),
	
	//###############
	// DeleteCurrentTrack
	//###############
	DESCRIPTION_DELETECURRENTTRACK("Remove the track currently playing from the database.\n"
			+ "In addition every entry of this track in playlists will also be removed"),
	SHORT_DESCRIPTION_DELETECURRENTTRACK("Delete the current track from the database"),
	
	//###############
	// DeletePlaylist
	//###############
	DESCRIPTION_DELETEPLAYLIST("Delete the chosen playlist.\n"
			+ "Private/User playlists can only be deleted by their creators\n"
			+ "Server playlists can only be deleted by admins"),
	SHORT_DESCRIPTION_DELETEPLAYLIST("Delete a playlist"),
	
	//###############
	// EQualizer
	//###############
	DESCRIPTION_EQUALIZER("Gruwie will display the current equalizer and its freq-bands.\n"
			+ "You can modify these bands by providing the optional parameters"),
	SHORT_DESCRIPTION_EQUALIZER("Show or modify the current equalizer"),
	OPTIONAL_PARAMETERS_EQUALIZER("Freq-Band between 1 and 15, Gain between -100% and +400%"),
	
	//###############
	// ExportPlaylist
	//###############
	DESCRIPTION_EXPORTPLAYLIST("Export/Save the current queue as an playlist with the provided name.\n"
			+ "There are two types of playlist:\n"
			+ "Guild/Server: Visible to everyone on this server, but only manageable for admins (create,delete,update)\n"
			+ "Private/User: Globally but tied to the creator)"),
	SHORT_DESCRIPTION_EXPORTPLAYLIST("Export/Save the current queue"),
	PARAMETERS_EXPORTPLAYLIST("The playlist-name with a length of up to 30 characters"),
	
	//###############
	// FastForward
	//###############
	DESCRIPTION_FASTFORWARD("You can customize the Fast-Forward-Time by providing an argument\n"
			+ "You can jump forward and backwards by using diffrent signs (*+ -*)"),
	SHORT_DESCRIPTION_FASTFORWARD("Skip some time in a track"),
	OPTIONAL_PARAMETERS_FASTFORWARD("Number n --> skip n-seconds (positive = forward <--> negative = backward)"),
	
	//###############
	// GetFilter
	//###############
	DESCRIPTION_GETFILTER("Get and load a filter, which can be applied to the music-equalizer.\n"
			+ "More filters will be added in the future\nThe Bot-Hoster can also add Custom-Filters\n"
			+ "If you want to add a certain filter just message either <@!" + ConfigManager.getString("owner_id") + "> or <@!690659763998031902>"),
	SHORT_DESCRIPTION_GETFILTER("Get all filters"),
	
	//###############
	// GetPlaylists
	//###############
	DESCRIPTION_GETPLAYLISTS("If you're using this command with a number Gruwie will load a random playlist of the given size\n"
			+ "By using only this command Gruwie will prompt a selectionmenu\n"
			+ "There are three types of playlists:\n"
			+ "***Guild-Playlists:*** which can only be played if you're on the right server\n"
			+ "***User-Playlists:*** which are private and bound to your account (these playlists can be used globally)\n"
			+ "***Random-Playlist:*** which can be used by everyone, Gruwie will try to retrieve up to n-Tracks depending on the config"),
	SHORT_DESCRIPTION_GETPLAYLISTS("Load saved playlists"),
	OPTIONAL_PARAMETERS_GETPLAYLISTS("**Number n** --> Load n random tracks"),
	
	//###############
	// Loop
	//###############
	DESCRIPTION_LOOP("If active playing a track will not affect the music-queue,"
			+ " if it is not active the next track will be pulled/removed from the music-queue"),
	SHORT_DESCRIPTION_LOOP("Change loop behaviour of the queue"),
	
	//###############
	// Lyrics
	//###############
	DESCRIPTION_LYRICS("By just using the command itself Gruwie will try to get the lyrics for the track currently playing\n"
			+ "By using *-command <interpret> - <title>* or *-command <title> - <interpret>* you can get the lyrics for the specific track"),
	SHORT_DESCRIPTION_LYRICS("Get Lyrics for tracks"),
	OPTIONAL_PARAMETERS_LYRICS("The song-name and the interpret seperated by **-**"),
	
	//###############
	// Next
	//###############
	DESCRIPTION_NEXT("Play the next track in the music-queue"),
	SHORT_DESCRIPTION_NEXT("Next Track in queue"),
	
	//###############
	// Play
	//###############
	DESCRIPTION_PLAY("By providing either a *youtube-track-url* or a *youtube-playlist-url* or a *search-query* you can load a track into the music-queue.\n"
			+ "An example for this command with a search-query would be: *-play darude sandstorm*"),
	SHORT_DESCRIPTION_PLAY("Play a track"),
	PARAMETERS_PLAY("Search-Query or a URL"),
	
	//###############
	// RemoveTrack
	//###############
	DESCRIPTION_REMOVETRACK("In addition to the command itself you have to provide a query, to identify the track you want to remove.\n"
			+ "If the result is a single track it will be removed immediately, else a dialog shows up with the possible options.\n"
			+ "If there are more than five results you have to provide a more accurate query"),
	SHORT_DESCRIPTION_REMOVETRACK("Remove track from queue"),
	
	//###############
	// ResumePause
	//###############
	DESCRIPTION_RESUMEPAUSE("Either pause or resume the playing of the track"),
	SHORT_DESCRIPTION_RESUMEPAUSE("Resume/Pause current track"),
	
	//###############
	// Scroll
	//###############
	DESCRIPTION_SCROLL("Scroll through the queue\n"
			+ "Has no effect if the queue is empty or completly displayed"),
	SHORT_DESCRIPTION_SCROLL("Scroll through the queue"),
	
	//###############
	// SetNextTrack
	//###############
	DESCRIPTION_SETNEXTTRACK("Set the next track which will be played\n"
			+ "If there's already one this won't work"),
	SHORT_DESCRIPTION_SETNEXTTRACK("Set the next track to play"),
	
	//###############
	// Showqueue
	//###############
	DESCRIPTION_SHOWQUEUE("Show complete queue, all messages of this type will be deleted during shutdown"),
	SHORT_DESCRIPTION_SHOWQUEUE("Show complete queue"),
	
	//###############
	// Shuffle
	//###############
	DESCRIPTION_SHUFFLE("Shuffle the complete music-queue"),
	SHORT_DESCRIPTION_SHUFFLE("Shuffle queue"),
	
	//###############
	// Stop
	//###############
	DESCRIPTION_STOP("Gruwie will do the following things: *Stop playing music, Clearing the music-queue, leaving the voice-channel*\n"
			+ "If noone except Gruwie is connected to a voice-channel this command will be executed automatically, if a track ends"),
	SHORT_DESCRIPTION_STOP("Stop playing"),
	
	//###############
	// UpdatePlaylist
	//###############
	DESCRIPTION_UPDATEPLAYLIST("***Replace*** the playlist with the current queue.\n"
			+ "Each track which has been in this playlist will be removed"),
	SHORT_DESCRIPTION_UPDATEPLAYLIST("Update a playlist with the current queue"),
	
	//###############
	// ShowEmotes
	//###############
	DESCRIPTION_SHOWEMOTES("Add all available emotes beneath the track-view\n"
			+ "those can be used like buttons"),
	SHORT_DESCRIPTION_SHOWEMOTES("Show emotes"),
	;

	private String value;
	
	Outputs(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
