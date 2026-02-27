package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageDrive;
import org.gametools.cleaner.StorageLocator;
import org.gametools.utilities.FileUtils;
import org.gametools.utilities.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class GetCompatdata implements ActionRunner {

    private final StorageLocator storageLocator;
    private final Function<StorageDrive, AppsRepository> appsRepositoryFactory;
    private final Long instanceId;

    public GetCompatdata(StorageLocator storageLocator,
                         Function<StorageDrive, AppsRepository> appsRepositoryFactory,
                         Long instanceId) {
        this.storageLocator = storageLocator;
        this.appsRepositoryFactory = appsRepositoryFactory;
        this.instanceId = instanceId;
    }

    @Override
    public void run() {
        final Map<Long, App> installedApps = getInstalledApps();

        for (StorageDrive drive : storageLocator.getDrives()) {
            try {
                String compatdataPath = drive.getCompatdataPath();
                Optional<Pair> first = Files.list(Path.of(compatdataPath))
                    .filter(Files::isDirectory)
                    .map(path -> new Pair(extractId(path), path))
                    .sorted(Comparator.comparingLong(value -> (long) value.key()))
                    .filter(pair -> pair.key().equals(instanceId))
                    .findFirst();

                if (first.isPresent()) {
                    long appId = (long) first.get().key();
                    App app = installedApps.get(appId);
                    if (app != null) {
                        app.printDetails(drive);
                    } else {
                        System.out.printf("%-11s %d%n", "Id:", appId);
                        System.out.printf("%-11s %s%n", "Name:", "(unknown)");
                        System.out.printf("%-11s %s%n", "Location:", "(unknown)");
                        System.out.printf("%-11s %s/%s (%s)%n", "Compatdata:", compatdataPath, appId, FileUtils.sizeFormatted(Path.of(compatdataPath, Long.toString(appId))));
                    }
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("No compatdata found");
    }

    // TODO this should be part of AppsRepository ?
    private Map<Long, App> getInstalledApps() {
        final Map<Long, App> installedApps = new HashMap<>();
        for (StorageDrive drive : storageLocator.getDrives()) {
            AppsRepository appsRepository = appsRepositoryFactory.apply(drive);
            for (App app : appsRepository.getApps()) {
                installedApps.put(app.id(), app);
            }
        }
        return installedApps;
    }

    private static long extractId(Path path) {
        return Long.parseLong(path.getFileName().toString());
    }

}
