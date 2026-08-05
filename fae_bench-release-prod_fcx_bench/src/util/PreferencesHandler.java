package util;

import java.util.prefs.Preferences;

public class PreferencesHandler {

	private static Preferences userPrefs = Preferences.userNodeForPackage(PreferencesHandler.class);

	private final static String MAT_FILE_LOCAL_DIR = "matFileLocalDir";
	private final static String MAT_FILE_REMOTE_DIR = "matFileRemoteDir";
	private final static String SETTINGS_DATABAE_URL = "settingsDataBaseUrl";
	private final static String SETTINGS_DATABAE_USER_NAME = "settingsDataBaseUserName";
	private final static String SETTINGS_DATABAE_PASSWORD = "settingsDataBasePassword";

	/**
	 * 
	 */
	public static void saveMatFileLocalDir(String dir) {
		userPrefs.put(MAT_FILE_LOCAL_DIR, dir);
	}

	/**
	 * 
	 */
	public static String readMatFileLocalDir() {
		String dir = userPrefs.get(MAT_FILE_LOCAL_DIR, "");
		return dir;
	}
	
	/**
	 * 
	 */
	public static void saveMatFileRemoteDir(String dir) {
		userPrefs.put(MAT_FILE_REMOTE_DIR, dir);
	}
	
	/**
	 * 
	 */
	public static String readMatFileRemoteDir() {
		String dir = userPrefs.get(MAT_FILE_REMOTE_DIR, "");
		return dir;
	}

	/**
	 * 
	 */
	public static void saveDataBaseUrl(String url) {
		userPrefs.put(SETTINGS_DATABAE_URL, url);
	}

	/**
	 * 
	 */
	public static String readDataBaseUrl() {
		String url = userPrefs.get(SETTINGS_DATABAE_URL, "");
		return url;
	}

	/**
	 * 
	 */
	public static void saveDataBaseUserName(String userName) {
		userPrefs.put(SETTINGS_DATABAE_USER_NAME, userName);
	}

	/**
	 * 
	 */
	public static String readDataBaseUserName() {
		String userName = userPrefs.get(SETTINGS_DATABAE_USER_NAME, "");
		return userName;
	}

	/**
	 * 
	 */
	public static void saveDataBasePassword(String password) {
		userPrefs.put(SETTINGS_DATABAE_PASSWORD, password);
	}

	/**
	 * 
	 */
	public static String readDataBasePassword() {
		String password = userPrefs.get(SETTINGS_DATABAE_PASSWORD, "");
		return password;
	}
}
