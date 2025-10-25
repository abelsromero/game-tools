package org.gametools.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SteamPaths {

    private static final String DEFAULT_INSTALLATION = ".local/share/Steam/steamapps";
    private static final String USER_DATA = ".local/share/Steam/userdata";
    // Storage Drives' index
    public static final String LIBRARY_FOLDERS = "libraryfolders.vdf";

    public static Path libraryFolders() {
        return home().resolve(DEFAULT_INSTALLATION).resolve(LIBRARY_FOLDERS);
    }

    public static Path userData() {
        // Ask user id as input
        Path userData = home().resolve(USER_DATA);
        try {
            return Files.list(userData)
                .filter(Files::isDirectory)
                .filter(path -> !path.getFileName().equals("0"))
                .findFirst()
                .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path home() {
        return Path.of(System.getProperty("user.home")).toAbsolutePath();
    }

}
