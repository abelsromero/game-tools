package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.AppsRepository;
import org.gametools.cleaner.StorageDrive;
import org.gametools.cleaner.StorageLocator;
import org.gametools.utilities.Pair;
import org.gametools.utilities.Shortcut;
import org.gametools.utilities.ShortcutsReader;
import org.gametools.utilities.SteamPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

import static org.gametools.utilities.FileUtils.sizeFormatted;

public class GetCompatdatas implements ActionRunner {

    private final ShortcutsReader shortcutsReader;
    private final StorageLocator storageLocator;
    private final Function<StorageDrive, AppsRepository> appsRepositoryFactory;

    public GetCompatdatas(ShortcutsReader shortcutsReader,
                          StorageLocator storageLocator,
                          Function<StorageDrive, AppsRepository> appsRepositoryFactory) {
        this.shortcutsReader = shortcutsReader;
        this.storageLocator = storageLocator;
        this.appsRepositoryFactory = appsRepositoryFactory;
    }


    @Override
    public void run() {
        final Map<Long, App> installedApps = getInstalledApps();
        final List<Shortcut> shortcuts = shortcutsReader.read(SteamPaths.userData().resolve("config"));

        storageLocator.getDrives()
            .forEach(drive -> {
                System.out.println();
                System.out.printf("= Library %s%n", drive.path());

                try {
                    Files.list(Path.of(drive.getCompatdataPath()))
                        .filter(Files::isDirectory)
                        .map(path -> new Pair(extractId(path), path))
                        .sorted(Comparator.comparingLong(value -> (long) value.key()))
                        .forEach(appData -> {
                            long appId = (long) appData.key();
                            Path path = (Path) appData.value();
                            App app = installedApps.get(appId);
                            if (app != null) {
                                app.printSummary();
                            } else {
                                Optional<Shortcut> nonSteamApp = shortcuts.stream()
                                    .filter(s -> s.getShortcutId() == (Long) appData.key())
                                    .findFirst();
                                if (nonSteamApp.isPresent()) {
                                    System.out.printf("%-10d %-25s %-10s%n", appId, "(non-steam) " + nonSteamApp.get().appName(), sizeFormatted(path));
                                } else {
                                    System.out.printf("%-10d %-25s %-10s%n", appId, "(uninstalled)", sizeFormatted(path));
                                }
                            }
                        });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
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
        // Non-Steam games have higher than int values because they use uint
        return Long.parseLong(path.getFileName().toString());
    }

}
