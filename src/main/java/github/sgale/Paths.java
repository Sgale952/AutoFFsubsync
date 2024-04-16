package github.sgale;

import static github.sgale.PropertyGenerator.getSetting;

public enum Paths {
    VIDEO(getSetting("videoPath")),
    SUBTITLES(getSetting("subtitlesPath")),
    DELETE_INIT_FILES(getSetting("deleteInitialFiles")),
    OVERLAY_ON_VIDEO(getSetting("overlayOnVideo"));

    private final String fieldKey;
    Paths(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getKey() {
        return fieldKey;
    }
}
