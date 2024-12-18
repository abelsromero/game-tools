package org.gametools.cleaner.actions;

import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageLocator;
import org.gametools.utilities.SteamPaths;

import java.util.Map;

public class RunnerResolver {

    public ActionRunner resolver(Action action) {

        return switch (action.command()) {
            case GET -> switch (action.subCommand()) {
                case LIBRARY -> new GetLibraries(Factories.getStorageLocator());
                case APP -> buildGetAppsActionRunner(action);
            };
            case LIST -> switch (action.subCommand()) {
                case LIBRARY, APP -> new VoidRunner();
            };
        };
    }

    private ActionRunner buildGetAppsActionRunner(Action action) {
        if (action.instanceId() == null) {
            return new GetApps(Factories.getStorageLocator(),
                storageDrive -> new AppsRepository(storageDrive.path()));
        } else {
            return new GetApp(Factories.getStorageLocator(),
                storageDrive -> new AppsRepository(storageDrive.path()),
                mapId(action.instanceId()));
        }
    }

    private static Integer mapId(String id) {
        return Integer.valueOf(id);
    }

    private final class Factories {

        private static StorageLocator getStorageLocator() {
            return new StorageLocator(SteamPaths.libraryFolders().toString());
        }
    }
}
