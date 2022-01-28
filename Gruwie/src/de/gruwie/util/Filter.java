package de.gruwie.util;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class Filter {

	public static ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, (error) -> {});
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	private static int KEEP_LOG_COUNT;
	
	public static void setErrStream() throws Exception {
		
		KEEP_LOG_COUNT = ConfigManager.getInteger("log_count");
		
		File directory = new File(".//logs");
		if(directory.exists() || directory.mkdir()) {
			deleteExcessiveFiles(directory.listFiles());
			String file_name = FORMAT.format(new Timestamp(System.currentTimeMillis()));
			File file = new File("logs//" + file_name + ".txt");
			if(file.exists()) file.delete();
			System.setErr(new Interceptor(file));
		}
	}
	
	private static void deleteExcessiveFiles(File[] files) {
		if(files.length > KEEP_LOG_COUNT) {
			for (int i = 0; i < files.length - (KEEP_LOG_COUNT - 1); i++) {
				files[i].delete();
			}
		}
		if(files.length == KEEP_LOG_COUNT) files[0].delete();
	}
	
}
