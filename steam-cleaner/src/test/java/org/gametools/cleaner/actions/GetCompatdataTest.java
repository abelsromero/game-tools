package org.gametools.cleaner.actions;

import org.gametools.cleaner.App;
import org.gametools.cleaner.StorageDrive;
import org.gametools.testing.TestLibraryTemplate;
import org.gametools.testing.TestResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gametools.cleaner.actions.Fakes.*;

class GetCompatdataTest {

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
    void should_return_compdata_for_installed_app() {
        var apps = List.of(app(1));
        final var storeDrive = new TestLibraryTemplate(TestResource.fromFile("steam-drive"))
            .create()
            .addCompadata(1)
            .get();
        final var fakeStorageLocator = fakeStorageLocator(List.of(
            new StorageDrive("0", storeDrive.root())
        ));

        final ActionRunner actionRunner = new GetCompatdata(fakeStorageLocator,
            it -> fakeAppsRepository(apps),
            1L);

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).matches("""
            Id:         1
            Name:       Game-01
            Location:   /tmp/game-tools-\\d+/steamapps/common/path/game/01
            Compatdata: /tmp/game-tools-\\d+/steamapps/compatdata/1 \\(0 MB\\)
            """);
    }

    @Test
    void should_return_compdata_for_uninstalled_app() {
        final List<App> apps = List.of();
        final var storeDrive = new TestLibraryTemplate(TestResource.fromFile("steam-drive"))
            .create()
            .addCompadata(1)
            .get();
        final var fakeStorageLocator = fakeStorageLocator(List.of(
            new StorageDrive("0", storeDrive.root())

        ));

        final ActionRunner actionRunner = new GetCompatdata(fakeStorageLocator,
            it -> fakeAppsRepository(apps),
            1L);

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).matches("""
            Id:         1
            Name:       \\(unknown\\)
            Location:   \\(unknown\\)
            Compatdata: /tmp/game-tools-\\d+/steamapps/compatdata/1 \\(0 MB\\)
            """);
    }

    @Test
    void should_return_not_found() {
        final List<App> apps = List.of();
        final var storeDrive = new TestLibraryTemplate(TestResource.fromFile("steam-drive")).create().get();
        final var fakeStorageLocator = fakeStorageLocator(List.of(
            new StorageDrive("0", storeDrive.root())
        ));

        final ActionRunner actionRunner = new GetCompatdata(fakeStorageLocator,
            it -> fakeAppsRepository(apps),
            1L);

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).isEqualTo("""
            No compatdata found
            """);
    }
}
