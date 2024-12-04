package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageLocator;

import java.util.List;

public class GetApps implements ActionRunner {

    private final StorageLocator storageLocator;

    public GetApps(StorageLocator storageLocator) {
        this.storageLocator = storageLocator;
    }

    @Override
    public void run() {
        List<App> apps = storageLocator.getDrives()
            .stream()
            .map(storageDrive -> new AppsRepository(storageDrive.path()))
            .flatMap(appsRepository1 -> appsRepository1.getApps().stream())
            .toList();

        if (apps.isEmpty()) {
            System.out.println("No apps found");
            return;
        } else {
            System.out.println("Found: " + storageLocator.getDrives().size());
        }

        apps.forEach(app -> {
            System.out.println("  %s: %s @ %s".formatted(app.id(), app.name(), app.installDir()));
        });
    }
}
