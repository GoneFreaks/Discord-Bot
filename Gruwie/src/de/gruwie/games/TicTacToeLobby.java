package de.gruwie.games;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;

public class TicTacToeLobby {

	private static HashMap<Long, TicTacToeLobby> storage = new HashMap<>();
	private static String[] emote_arr = {"1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"};
	
	private String player1;
	private String player2;
	private long guildId;
	
	private TicTacToeGame game;
	private int turn;
	private Message game_view;
	
	public TicTacToeLobby(String player1, String player2, long guildId) {
		this.player1 = player1;
		this.player2 = player2;
		this.guildId = guildId;
		this.turn = 1;
		storage.put(guildId, this);
		this.game = new TicTacToeGame(player1, player2);
		
		this.game_view = MessageManager.sendEmbedMessage(game.toString(), guildId, false);
		addReactions();
	}
	
	private void addReactions() {
		for (int i = 0; i < emote_arr.length; i++) {
			this.game_view.addReaction(emote_arr[i]).queue();
		}
	}
	
	public static TicTacToeLobby getLobbyByGuildId(long guildId) {
		return storage.get(guildId);
	}
	
	public static boolean lobbyExists(long guildId) {
		return storage.containsKey(guildId);
	}
	
	public void doTurn(long messageId, String playerId, String emote) {
		
		if(game_view.getIdLong() == messageId) {
			if(playerId.equals(player1) || playerId.equals(player2)) {
				if(setPlayer(playerId, Integer.parseInt(emote.charAt(0) + ""))) this.game_view.clearReactions(emote).queue();
			}
		}
	}
	
	private boolean setPlayer(String playerId, int index) {
		
		if(game.getContent(index) == '-') {
			turn++;
			if(turn % 2 == 0) game.setPlayer1(index);
			else game.setPlayer2(index);
			MessageManager.editMessage(game_view, game.toString());
			return isGameEnd();
		}
		return false;
	}
	
	private boolean isGameEnd() {
		if(game.isWinner() || turn > 9) {
			endLobby();
			return false;
		}
		else return true;
	}
	
	public void endLobby() {
		storage.remove(guildId);
		game_view.clearReactions().queue();
		game_view.delete().queueAfter(15, TimeUnit.SECONDS);
	}
	
}
