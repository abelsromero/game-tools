package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageDrive;
import org.gametools.cleaner.StorageLocator;

import java.util.Optional;
import java.util.function.Function;

public class GetApp implements ActionRunner {

    private final StorageLocator storageLocator;
    private final Function<StorageDrive, AppsRepository> appsRepositoryFactory;
    private final Integer instanceId;

    public GetApp(StorageLocator storageLocator,
                  Function<StorageDrive, AppsRepository> appsRepositoryFactory,
                  Integer instanceId) {
        this.storageLocator = storageLocator;
        this.appsRepositoryFactory = appsRepositoryFactory;
        this.instanceId = instanceId;
    }

    @Override
    public void run() {
        for (StorageDrive drive : storageLocator.getDrives()) {
            AppsRepository appsRepository = appsRepositoryFactory.apply(drive);
            Optional<App> candidate = appsRepository.getApp(instanceId);
            if (candidate.isPresent()) {
                App app = candidate.get();
                System.out.printf("%-10s %-10d%n", "Id:", app.id());
                System.out.printf("%-10s %-10s%n", "Name:", app.name());
                System.out.printf("%-10s %-10s/%s%n", "Location:", drive.getAppsPath(),app.installDir());
                return;
            }
        }
    }
}
