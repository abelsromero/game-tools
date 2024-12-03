package org.gametools.utilities;

import java.nio.file.Path;

public class SteamPaths {

    private static final String DEFAULT_INSTALLATION = ".local/share/Steam/steamapps";
    // Storage Drives' index
    public static final String LIBRARY_FOLDERS = "libraryfolders.vdf";

    public static Path libraryFolders() {
        return Path.of(System.getProperty("user.home")).resolve(DEFAULT_INSTALLATION).resolve(LIBRARY_FOLDERS);
    }

}
