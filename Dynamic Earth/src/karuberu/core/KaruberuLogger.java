package karuberu.core;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import karuberu.core.asm.ObfHelper;

import cpw.mods.fml.common.FMLLog;

public final class KaruberuLogger {
	
	private static final boolean debugEnabled = !ObfHelper.obfuscated;
	
	private static final String
		sourceString = "KaruberuCore";
	private static final Logger
		logger = Logger.getLogger(sourceString);
	static {
		logger.setParent(FMLLog.getLogger());
	}

	public static void log(Level level, String message) {
		logger.log(level, message);
	}
	
	public static void fine(String message) {
		logger.log(Level.FINE, message);
	}
	
	public static void finer(String message) {
		logger.log(Level.FINER, message);
	}
	
	public static void finest(String message) {
		logger.log(Level.FINEST, message);
	}
	
	public static void debug(Object... objects) {
		if (debugEnabled) {
			StringBuilder message = new StringBuilder();
			for (Object object : objects) {
				if (object == null) {
					message.append("null");
				} else if (object instanceof Object[]) {
					Object[] array = (Object[])object;
					message.append(Arrays.toString(array));
				} else if (object instanceof int[]) {
					int[] array = (int[])object;
					message.append(Arrays.toString(array));
				} else {
					message.append(object.toString());
				}
				message.append(" ");
			}
			message.deleteCharAt(message.length() - 1);
			logger.log(Level.INFO, message.toString());
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}
}
