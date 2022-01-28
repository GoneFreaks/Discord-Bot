package de.gruwie.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GruwieIO {
	
	private static InputStream getInputStream (String link) {
		try {
			URL url = new URL(link);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if(responseCode == 200) {
				return con.getInputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
