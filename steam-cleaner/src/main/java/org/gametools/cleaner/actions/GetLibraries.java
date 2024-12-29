package org.gametools.cleaner.actions;

import org.gametools.cleaner.StorageDrive;
import org.gametools.cleaner.StorageLocator;

import java.util.List;

public class GetLibraries implements ActionRunner {

    private final StorageLocator storageLocator;

    public GetLibraries(StorageLocator storageLocator) {
        this.storageLocator = storageLocator;
    }

    @Override
    public void run() {
        List<StorageDrive> drives = storageLocator.getDrives();

        if (drives.isEmpty()) {
            System.out.println("No libraries found");
            System.exit(0);
        } else {
            System.out.println("Found: " + drives.size());
        }

        drives.forEach(storageDrive -> {
            System.out.println("  %s: %s".formatted(storageDrive.id(), storageDrive.path()));
        });
    }
}
