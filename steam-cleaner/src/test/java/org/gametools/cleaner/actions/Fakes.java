package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageDrive;
import org.gametools.cleaner.StorageLocator;

import java.util.List;

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
        };
    }
}
