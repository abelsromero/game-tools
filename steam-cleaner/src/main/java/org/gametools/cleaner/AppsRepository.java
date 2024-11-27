package org.gametools.cleaner;

import org.gametools.utilities.VdfParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// TODO make CompositeAppRepository to handle multiple repositories together
class AppsRepository {

    private static final String APPS_DIR = "steamapps";

    private static final String INSTALL_DIR = "common";
    private static final String COMPAT_DATA_DIR = "compatdata";

    private final Path appsRoot;

    public AppsRepository(String path) {
        this.appsRoot = Path.of(path).resolve(APPS_DIR);
    }

    public List<App> getApps() {
        final VdfParser parser = new VdfParser();

        return installedAppsManifests()
            .map(path -> parser.parse(path.toString()))
            .map(vdfFile -> {
                Map<String, String> appState = (Map<String, String>) vdfFile.properties().get("AppState");
                return new App(
                    Integer.valueOf(appState.get("appid")),
                    appState.get("name"),
                    appState.get("installdir")
                );
            })
            .toList();
    }

    private Stream<Path> installedAppsManifests() {
        return listFiles(appsRoot)
            .filter(path -> path.toString().endsWith(".acf") && !path.toFile().isDirectory());
    }

    /**
     * Paths that do not match any appmanifest in the storage.
     */
    public List<Path> findOrphanCompatdata() {
        // No other folders are expected
        final List<String> appIds = installedAppsManifests()
            .map(path -> {
                String filename = path.getFileName().toString();
                return filename.substring(filename.lastIndexOf('_') + 1, filename.length() - 4);
            })
            .toList();

        return listFiles(appsRoot.resolve(COMPAT_DATA_DIR))
            .filter(path -> path.toFile().isDirectory())
            .filter(path -> !appIds.contains(path.getFileName().toString()))
            .toList();
    }

    private Stream<Path> listFiles(Path path) {
        try {
            return Files.list(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

record App(Integer id, String name, String installDir) {
}
