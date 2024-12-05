package org.gametools.cleaner.actions;

import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageLocator;
import org.gametools.utilities.SteamPaths;

public class RunnerResolver {

    public ActionRunner resolver(Action action) {

        return switch (action.command()) {
            case GET -> switch (action.subCommand()) {
                case LIBRARY -> new GetLibraries(Factories.getStorageLocator());
                case APP -> new GetApps(Factories.getStorageLocator(),
                    storageDrive -> new AppsRepository(storageDrive.path()));
            };
            case LIST -> switch (action.subCommand()) {
                case LIBRARY, APP -> new VoidRunner();
            };
        };
    }


    private final class Factories {

        private static StorageLocator getStorageLocator() {
            return new StorageLocator(SteamPaths.libraryFolders().toString());
        }
    }
}
