package de.gruwie.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class Threadpool {

	private static ExecutorService exc = Executors.newCachedThreadPool(new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			t.setName("EXC");
			return t;
		}
	});
	
	public static Future<String> submit (Callable<String> c){
		return exc.submit(c);
	}
	
	public static void execute (Runnable r) {
		exc.execute(r);
	}
	
	public static void shutdown() {
		GruwieUtilities.log();
		exc.shutdownNow();
	}
	
}
