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
    private final Long instanceId;

    public GetApp(StorageLocator storageLocator,
                  Function<StorageDrive, AppsRepository> appsRepositoryFactory,
                  Long instanceId) {
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
                candidate.get().printDetails(drive);
                return;
            }
        }
        System.out.println("No app found");
    }

}
