package org.gametools.cleaner;

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


    // notes: seems linux and win uninstalling cleans install and comptdata
    @Test
    void should_return_orphans() {
        final String storageDrive = "/media/XXXX/games/steam";
//        final String storageDrive = TestResource.fromFile(storage);

        AppsRepository repository = new AppsRepository(storageDrive);

        List<Path> paths = repository.findOrphanCompdata();

        // 292030, The Witcher 3: Wild Hunt
        // 487390, System Shock Demo
        // 22320, Morrowind
        // 3111910, Gedonia 2 Demo
        // 228280, Baldur's Gate: Enhanced Edition
        // 1544020, The Callisto Protocol
        // 2123870, Dungeon Drafters Demo
        // 43515, Divinity: Original Sin 2

        // 275850, No Man's Sky
        // 291650, Pillars of Eternity
        // 560130, Pillars of Eternity II

        System.out.println("Hello");
    }

}
