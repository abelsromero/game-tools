package org.gametools.utilities;

public record Shortcut(int appId, String appName, String launchOptions) {

    public long getShortcutId() {
        return appId & 0xFFFFFFFFL;  // <— add this
    }

}
