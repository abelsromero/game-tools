package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageDrive;
import org.gametools.cleaner.StorageLocator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GetApps implements ActionRunner {

    private final StorageLocator storageLocator;
    private final Function<StorageDrive, AppsRepository> appsRepositoryFactory;

    public GetApps(StorageLocator storageLocator,
                   Function<StorageDrive, AppsRepository> appsRepositoryFactory) {
        this.storageLocator = storageLocator;
        this.appsRepositoryFactory = appsRepositoryFactory;
    }

    @Override
    public void run() {
        final Map<String, List<App>> installedApps = new HashMap<>();
        for (StorageDrive drive : storageLocator.getDrives()) {
            AppsRepository appsRepository = appsRepositoryFactory.apply(drive);
            installedApps.put(drive.path(), appsRepository.getApps());
        }

        int count = installedApps.values().stream().map(List::size).reduce(0, Integer::sum);
        if (count == 0) {
            System.out.println("No apps found");
            return;
        } else {
            System.out.println("Found: " + count);
        }

        installedApps.forEach((key, value) -> {
            System.out.println();
            System.out.printf("= Library %s%n", key);
            value.stream()
                .sorted(Comparator.comparing(App::name))
                .forEach(app -> {
                    System.out.printf("%-10d %-10s%n", app.id(), app.name());
                });
        });
    }
}
