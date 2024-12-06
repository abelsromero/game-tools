package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.StorageDrive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gametools.cleaner.actions.Fakes.fakeAppsRepository;
import static org.gametools.cleaner.actions.Fakes.fakeStorageLocator;

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
        var apps = List.of(app(1), app(2), app(3), app(4));
        final var fakeStorageLocator = fakeStorageLocator(List.of(
            new StorageDrive("0", "/home/me/.steam"),
            new StorageDrive("1", "/mount/other/games")
        ));
        final ActionRunner actionRunner = new GetApps(fakeStorageLocator, it -> fakeAppsRepository(apps));

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).isEqualTo("""
            Found: 8
            
            = Library /home/me/.steam
            1          Game-01  \s
            2          Game-02  \s
            3          Game-03  \s
            4          Game-04  \s
            
            = Library /mount/other/games
            1          Game-01  \s
            2          Game-02  \s
            3          Game-03  \s
            4          Game-04  \s
            """);
    }

    @Test
    void should_return_zero_apps() {
        final var fakeStorageLocator = fakeStorageLocator(List.of());
        final var actionRunner = new GetLibraries(fakeStorageLocator);

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).isEqualTo("""
            No libraries found
            """);
    }

    private static App app(int id) {
        return new App(id, "Game-0" + id, "/path/game/0" + id);
    }

}
