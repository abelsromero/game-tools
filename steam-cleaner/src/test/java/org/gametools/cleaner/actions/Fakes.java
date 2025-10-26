package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageDrive;
import org.gametools.cleaner.StorageLocator;

import java.util.List;
import java.util.Optional;

class Fakes {

    static StorageLocator fakeStorageLocator(List<StorageDrive> expected) {
        return new StorageLocator(null) {
            public List<StorageDrive> getDrives() {
                return expected;
            }
        };
    }

    static AppsRepository fakeAppsRepository(List<App> expected) {

        return new AppsRepository("/fake/path") {
            public List<App> getApps() {
                return expected;
            }

            public Optional<App> getApp(Long appId) {
                return expected.stream().filter(app -> app.id().equals(appId)).findFirst();
            }
        };
    }

    static App app(long id) {
        return new App(id, "Game-0" + id, "path/game/0" + id);
    }

}
