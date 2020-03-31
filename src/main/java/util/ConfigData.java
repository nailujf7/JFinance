package util;

import controller.LoginScreenController;

import java.util.prefs.Preferences;

public class ConfigData {

    private static Preferences userPreferences = Preferences.userNodeForPackage(LoginScreenController.class);

    public static void setPrefData(String key, String value) {
        userPreferences.put(key, value);
    }

    public static boolean userDataExist() {
        String value = userPreferences.get("data", "unsaved");
        if (value.equals("saved")) {
            return true;
        } else {
            return false;
        }
    }

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

    public static String loadPrefData(String key, String defaultValue) {
        return userPreferences.get(key, defaultValue);
    }

}
