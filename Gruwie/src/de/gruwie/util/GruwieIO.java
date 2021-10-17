package de.gruwie.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.gruwie.util.dto.ErrorDTO;

public class GruwieIO {

	public static void writeToFile(String file, String output) {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			writer.write(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> readFromFile(String file) {
		List<String> result = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			String temp;
			while((temp = reader.readLine()) != null) result.add(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-GRUWIE-IO", "SYSTEM", link));
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
