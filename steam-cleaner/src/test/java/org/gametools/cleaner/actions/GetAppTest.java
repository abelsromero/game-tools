package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.StorageDrive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gametools.cleaner.actions.Fakes.*;

class GetAppTest {

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
    void should_return_a_single_app() {
        var apps = List.of(app(1), app(2), app(3), app(4));
        final var fakeStorageLocator = fakeStorageLocator(List.of(
            new StorageDrive("0", "/home/me/.steam"),
            new StorageDrive("1", "/mount/other/games")
        ));

        final ActionRunner actionRunner = new GetApp(fakeStorageLocator, it -> fakeAppsRepository(apps), 1);

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).isEqualTo("""
            Id:         1
            Name:       Game-01
            Location:   /home/me/.steam/steamapps/common//path/game/01
            Compatdata: /home/me/.steam/steamapps/compatdata/1
            """);
    }

    @Test
    void should_return_not_found() {
        final List<App> apps = List.of();
        final var fakeStorageLocator = fakeStorageLocator(List.of(
            new StorageDrive("0", "/home/me/.steam"),
            new StorageDrive("1", "/mount/other/games")
        ));

        final ActionRunner actionRunner = new GetApp(fakeStorageLocator, it -> fakeAppsRepository(apps), 1);

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).isEqualTo("""
            No app found
            """);
    }

}
