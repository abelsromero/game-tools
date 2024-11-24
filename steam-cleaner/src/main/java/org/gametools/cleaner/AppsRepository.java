package org.gametools.cleaner;

import org.gametools.utilities.VdfParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class AppsRepository {

    private static final String APPS_DIR = "steamapps";

    private static final String INSTALL_DIR = "common";
    private static final String COMP_DATA_DIR = "compatdata";

    private final Path appsRoot;

    public AppsRepository(String path) {
        this.appsRoot = Path.of(path).resolve(APPS_DIR);
    }

    List<App> getApps() {
        final VdfParser parser = new VdfParser();

        return listFiles(appsRoot)
            .filter(path -> path.toString().endsWith(".acf") && !path.toFile().isDirectory())
            .map(path -> parser.parse(path.toString()))
            .map(vdfFile -> {
                Map<String, String> appState = (Map<String, String>) vdfFile.properties().get("AppState");
                Integer appid = Integer.valueOf(appState.get("appid"));
                String name = appState.get("name");
                String installdir = appState.get("installdir");
                return new App(appid, name, installdir);
            })
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
