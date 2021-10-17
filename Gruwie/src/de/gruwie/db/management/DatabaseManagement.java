package de.gruwie.db.management;

import java.util.List;
import java.util.Scanner;

import de.gruwie.db.ConnectionManager;
import de.gruwie.util.GruwieIO;

public class DatabaseManagement {

	private static final String[] GENRE = {"ROCK", "METAL", "POP", "RAP", "SCHLAGER", "KLASSIK", "ELEKTRO", "JAZZ"};
	
	public static void boot() {
		try {
			
			String genreString = createGenreString();
			String file = System.currentTimeMillis() + ".txt";
			
			if(ConnectionManager.createConnection()) {
				try(Scanner sc = new Scanner(System.in)){
					List<TrackDTO> result = DataAccess.readTracks();
					for (TrackDTO i : result) {
						System.out.println("CHOOSE UP TO 3 GENRES FOR THE CURRENT TRACK\nTYPE 0 IF YOU WANT TO JUMP TO THE NEXT TRACK LEAVING ALL REMAINING FIELDS EMPTY\nTYPE -1 TO EXIT THIS WHOLE PROCEDURE\n");
						System.out.println(genreString);
						System.out.println(i.getUrl());
						for (int j = 0; j < 3; j++) {
							int input = sc.nextInt();
							if(input < 0) return;
							if(input == 0) break;
							if(input > GENRE.length) return;
							i.setGenre(input-1, j);
							System.out.println(GENRE[input-1]);
						}
						System.out.println("\n\n");
						GruwieIO.writeToFile(file, i + "\n\n");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String createGenreString() {
		StringBuilder b = new StringBuilder("");
		for (int i = 0; i < GENRE.length; i++) {
			b.append((i+1) + ":" + GENRE[i] + ((i+1==GENRE.length)? "\n" : "\t"));
		}
		return b.toString();
	}
	
}
