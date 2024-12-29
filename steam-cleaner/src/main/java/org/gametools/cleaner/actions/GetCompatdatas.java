package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageDrive;
import org.gametools.cleaner.StorageLocator;
import org.gametools.utilities.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.gametools.utilities.FileUtils.sizeFormatted;

public class GetCompatdatas implements ActionRunner {

    private final StorageLocator storageLocator;
    private final Function<StorageDrive, AppsRepository> appsRepositoryFactory;

    public GetCompatdatas(StorageLocator storageLocator,
                          Function<StorageDrive, AppsRepository> appsRepositoryFactory) {
        this.storageLocator = storageLocator;
        this.appsRepositoryFactory = appsRepositoryFactory;
    }

    @Override
    public void run() {
        final Map<Integer, App> installedApps = getInstalledApps();

        storageLocator.getDrives()
            .forEach(drive -> {
                System.out.println();
                System.out.printf("= Library %s%n", drive.path());

                try {
                    Files.list(Path.of(drive.getCompatdataPath()))
                        .filter(Files::isDirectory)
                        .map(path -> new Pair(extractId(path), path))
                        .sorted(Comparator.comparingInt(value -> (int) value.key()))
                        .forEach(appData -> {
                            int appId = (int) appData.key();
                            Path path = (Path) appData.value();
                            App app = installedApps.get(appId);
                            if (app != null) {
                                app.printSummary();
                            } else {
                                System.out.printf("%-10d %-15s %-10s%n", appId, "(uninstalled)", sizeFormatted(path));
                            }
                        });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    // TODO this should be part of AppsRepository ?
    private Map<Integer, App> getInstalledApps() {
        final Map<Integer, App> installedApps = new HashMap<>();
        for (StorageDrive drive : storageLocator.getDrives()) {
            AppsRepository appsRepository = appsRepositoryFactory.apply(drive);
            for (App app : appsRepository.getApps()) {
                installedApps.put(app.id(), app);
            }
        }
        return installedApps;
    }

    private static int extractId(Path path) {
        return Integer.parseInt(path.getFileName().toString());
    }

}
