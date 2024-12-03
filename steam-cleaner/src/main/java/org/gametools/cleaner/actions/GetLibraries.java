package org.gametools.cleaner.actions;

import org.gametools.cleaner.StorageLocator;
import org.gametools.utilities.SteamPaths;

public class GetLibraries implements ActionRunner {

    @Override
    public void run() {
        new StorageLocator(SteamPaths.libraryFolders().toString())
            .getDrives()
            .forEach(storageDrive -> {
                System.out.println(storageDrive.path());
            });
    }
}
