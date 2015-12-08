package karuberu.dynamicearth;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;

public final class DELogger {
	private static final boolean
		debugEnabled = false;
	private static final String
		sourceString = "DynamicEarth";
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
	
	public static void warning(String message) {
		logger.log(Level.WARNING, message);
	}
	
	public static void severe(String message) {
		logger.log(Level.SEVERE, message);
	}
	
	@SuppressWarnings("rawtypes")
	public static void debug(Object... objects) {
		if (debugEnabled) {
			StringBuilder message = new StringBuilder();

			for (Object object : objects) {
				if (object == null) {
					message.append("null");
				} else if (object instanceof Object[] || object instanceof List) {
					Object[] array;
					if (object instanceof List) {
						array = ((List)object).toArray();
					} else {
						array = (Object[])object;
					}
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
