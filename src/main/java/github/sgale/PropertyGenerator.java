package github.sgale;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyGenerator {
    private static final String SETTINGS_FILE = "autoFFsubsync.properties";
    private static final Properties properties = new Properties();

    public static void loadSettingsFile() {
        try (FileInputStream propertyFile = new FileInputStream(SETTINGS_FILE)) {
            properties.load(propertyFile);
        }
        catch (IOException e) {
            if(!Files.exists(Paths.get(SETTINGS_FILE))) {
                setDefaultSettings();
                System.exit(0);
            }
        }
    }

    private static void setDefaultSettings() {
        setSetting("videoPath", "D:\\Box\\Japanese\\Anime");
        setSetting("subtitlesPath", "D:\\Box\\Japanese\\Subs");
        setSetting("deleteInitialSubtitles", "true");
        setSetting("overlayOnVideo", "false");

        saveSettingsFile();
    }

    private static void saveSettingsFile() {
        try (FileOutputStream out = new FileOutputStream(SETTINGS_FILE)) {
            properties.store(out, "https://github.com/Sgale952/AutoFFsubsync");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSetting(String key) {
        return checkProperty(key);
    }

    private static String checkProperty(String key) {
        String property = properties.getProperty(key);
        if(property==null) {
            System.exit(0);
        }
        return property;
    }

    private static void setSetting(String key, String value) {
        properties.setProperty(key, value);
    }
}

