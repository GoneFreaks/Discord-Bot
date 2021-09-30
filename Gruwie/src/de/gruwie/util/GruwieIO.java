package de.gruwie.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GruwieIO {

	public static void writeToFile(String file, String output) {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {

			writer.write(output);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static InputStream getInputStream (String link) {
		try {
			URL url = new URL(link);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if(responseCode == 200) {
				return con.getInputStream();
			}
			else {
				switch (responseCode) {
				case 403:
					System.out.println("Access denied");
					break;
					
				case 404:
					System.out.println("Not found");
					break;
					
				case 429:
					System.out.println("Too many requests");
					break;

				default:
					System.out.println("Response Fehlercode: " + responseCode);
				}
			}
		} catch (Exception e) {
			//ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-GRUWIE-IO", "SYSTEM", link));
		}
		return null;
	}
	
	public static String doWebBrowsing(String input) throws Exception {
		
		StringBuilder b = new StringBuilder("");
		InputStream website = getInputStream(input);
		if(website == null) return null;
		try (Scanner in = new Scanner(website)){
			while (in.hasNext()) {
				String line = in.nextLine();
				b.append(line);
				if(line.contains("<!-- MxM banner -->")) break;
			}
				
		}
		String edit = b.toString().replace(';', '\n');
		return edit;
	}
	
}
