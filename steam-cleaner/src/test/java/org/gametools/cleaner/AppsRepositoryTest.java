package org.gametools.cleaner;

import org.gametools.testing.TestLibraryTemplate;
import org.gametools.testing.TestResource;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AppsRepositoryTest {

    @Test
    void should_return_apps() {
        final String storageDrive = TestResource.fromFile("steam-drive");

        // TODO inject StorageLocator to mock it?
        AppsRepository repository = new AppsRepository(storageDrive);
        List<App> apps = repository.getApps();

        assertThat(apps).containsExactlyInAnyOrder(
            new App(1070560, "Steam Linux Runtime 1.0 (scout)", "SteamLinuxRuntime"),
            new App(1887720, "Proton 7.0", "Proton 7.0"),
            new App(208580, "STAR WARS™ Knights of the Old Republic™ II: The Sith Lords™", "Knights of the Old Republic II"),
            new App(220, "Half-Life 2", "Half-Life 2"),
            new App(2348590, "Proton 8.0", "Proton 8.0"),
            new App(2373630, "Moonring", "Moonring"),
            new App(578650, "The Outer Worlds", "TheOuterWorlds")
        );
    }

    // notes: seems linux and win uninstalling cleans install and compatdata
    @Test
    void should_return_orphans() {
        final var storeDrive = new TestLibraryTemplate(TestResource.fromFile("steam-drive"))
            .create()
            .addCompadata(42)
            .addCompadata(46)
            .addCompadata(24)
            .addCompadata(64)
            .get();

        AppsRepository repository = new AppsRepository(storeDrive.root());
        List<Path> paths = repository.findOrphanCompatdata();

        final Path expectedCompatdata = storeDrive.compatdata();
        assertThat(paths)
            .containsExactlyInAnyOrder(
                resolve(expectedCompatdata, 42),
                resolve(expectedCompatdata, 46),
                resolve(expectedCompatdata, 24),
                resolve(expectedCompatdata, 64)
            );
    }

    private static Path resolve(Path basePath, int appId) {
        return basePath.resolve(Integer.toString(appId));
    }

}
