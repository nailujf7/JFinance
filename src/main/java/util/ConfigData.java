package util;

import controller.LoginScreenController;

import java.util.prefs.Preferences;

/**
 * ConfigData class to manage configuration of user such as remembering user credentials or theme color
 */
public class ConfigData {

    private static Preferences userPreferences = Preferences.userNodeForPackage(LoginScreenController.class);

    /**
     * Checks if user data and preferences is saved
     * @return
     */
    public static boolean userDataExist() {
        String value = userPreferences.get("data", "unsaved");
        if (value.equals("saved")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets default data and preferences
     */
    public static void setDefaultData() {
        if (userDataExist()) {
            setPrefData("username", "");
            setPrefData("password", "");
            setPrefData("theme", Constants.STANDARD_THEME);
            setPrefData("profile", Constants.IMAGE_PROFILE);
            setPrefData("mail_username", "");
            setPrefData("mail_password", "");
            setPrefData("profile", Constants.IMAGE_PROFILE);

        }
    }

    /**
     * Loads user preferences
     * @param key
     * @param defaultValue
     * @return
     */
    public static String loadPrefData(String key, String defaultValue) {
        return userPreferences.get(key, defaultValue);
    }

    /**
     * Sets user preferences
     * @param key
     * @param value
     */
    public static void setPrefData(String key, String value) {
        userPreferences.put(key, value);
    }

}
