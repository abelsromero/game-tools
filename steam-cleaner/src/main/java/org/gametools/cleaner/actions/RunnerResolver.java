package org.gametools.cleaner.actions;

import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageDrive;
import org.gametools.cleaner.StorageLocator;
import org.gametools.utilities.ShortcutsReader;
import org.gametools.utilities.SteamPaths;

import java.util.function.Function;

public class RunnerResolver {

    public ActionRunner resolver(Action action) {

        return switch (action.command()) {
            case GET -> switch (action.subCommand()) {
                case APP -> buildGetAppsActionRunner(action);
                case COMPATDATA -> buildGetCompatdatasActionRunner(action);
                case LIBRARY -> new GetLibraries(Factories.getStorageLocator());
            };
        };
    }

    private ActionRunner buildGetAppsActionRunner(Action action) {
        if (action.instanceId() == null) {
            return new GetApps(Factories.getStorageLocator(),
                Factories.getAppsRepositoryFactory());
        } else {
            return new GetApp(Factories.getStorageLocator(),
                Factories.getAppsRepositoryFactory(),
                mapId(action.instanceId()));
        }
    }

    private ActionRunner buildGetCompatdatasActionRunner(Action action) {
        if (action.instanceId() == null) {
            return new GetCompatdatas(
                Factories.getShortcutsReader(),
                Factories.getStorageLocator(),
                Factories.getAppsRepositoryFactory());
        } else {
            return new GetCompatdata(Factories.getStorageLocator(),
                Factories.getAppsRepositoryFactory(),
                mapId(action.instanceId()));
        }
    }

    private static Long mapId(String id) {
        return Long.valueOf(id);
    }

    private final class Factories {

        private static ShortcutsReader getShortcutsReader() {
            return new ShortcutsReader();
        }

        private static StorageLocator getStorageLocator() {
            return new StorageLocator(SteamPaths.libraryFolders().toString());
        }

        private static Function<StorageDrive, AppsRepository> getAppsRepositoryFactory() {
            return storageDrive -> new AppsRepository(storageDrive.path());
        }
    }
}
