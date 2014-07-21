package hu.hajnaldavid.android.bkvmegallok.util;

public class Settings {

	private static final String DEV_URL = "http://10.0.2.2/android/bkk-api/";
	private static final String TEST_URL = "http://<yourdomain.here>/android/bkk-api/";

	public static String getUrl() {
		return TEST_URL;
	}
}
