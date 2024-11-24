package org.gametools.cleaner;

import org.gametools.testing.TestResource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AppsRepositoryTest {

    @Test
    void should_return_apps() {
        final String storageDrive = TestResource.fromFile("steam-drive");

        // TODO inject StorageLocator to mock it?
        AppsRepository appsRepository = new AppsRepository(storageDrive);

        List<App> apps = appsRepository.getApps();

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
}
