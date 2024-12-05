package org.gametools.cleaner.actions;

import org.gametools.cleaner.StorageDrive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gametools.cleaner.actions.Fakes.fakeStorageLocator;

class GetLibrariesTest {

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
    void should_return_all_libraries() {
        final var fakeStorageLocator = fakeStorageLocator(List.of(
            new StorageDrive("0", "/home/me/.steam"),
            new StorageDrive("1", "/mount/other/games")
        ));
        final var actionRunner = new GetLibraries(fakeStorageLocator);

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).isEqualTo("""
            Found: 2
              0: /home/me/.steam
              1: /mount/other/games
            """);
    }

    @Test
    void should_return_zero_libraries() {
        final var fakeStorageLocator = fakeStorageLocator(List.of());
        final var actionRunner = new GetLibraries(fakeStorageLocator);

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).isEqualTo("""
            No libraries found
            """);
    }

}
