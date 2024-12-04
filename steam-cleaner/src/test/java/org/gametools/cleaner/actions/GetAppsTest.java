package org.gametools.cleaner.actions;

import org.gametools.cleaner.StorageDrive;
import org.gametools.cleaner.StorageLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetAppsTest {

    private OutputHandler output;

    @BeforeEach
    void beforeEach() {
        output = OutputHandler.start();
    }

    @AfterEach
    void afterEach() {
        output.release();
    }

    @Test
    void should_return_all_apps() {
        final var fakeStorageLocator = getFakeStorageLocator(List.of(
            new StorageDrive("0", "/home/me/.steam"),
            new StorageDrive("1", "/mount/other/games")
        ));
        final ActionRunner actionRunner = new GetApps(fakeStorageLocator);

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).isEqualTo("""
            Found: 2
              0: /home/me/.steam
              1: /mount/other/games
            """);
    }

    @Test
    void should_return_zero_apps() {
        final var fakeStorageLocator = getFakeStorageLocator(List.of());
        final var actionRunner = new GetLibraries(fakeStorageLocator);

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).isEqualTo("""
            No libraries found
            """);
    }

    private static StorageLocator getFakeStorageLocator(List<StorageDrive> expected) {
        return new StorageLocator(null) {
            public List<StorageDrive> getDrives() {
                return expected;
            }
        };
    }

}
