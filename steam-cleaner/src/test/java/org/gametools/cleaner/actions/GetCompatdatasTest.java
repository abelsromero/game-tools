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

class GetCompatdatasTest {

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
    void should_return_all_compdatas() {
        var apps = List.of(app(1), app(4), app(5), app(8));
        final var storeDrive1 = new TestLibraryTemplate(TestResource.fromFile("steam-drive"))
            .create()
            .addCompadata(1)
            .addCompadata(2)
            .addCompadata(3)
            .addCompadata(4)
            .get();
        final var storeDrive2 = new TestLibraryTemplate(TestResource.fromFile("steam-drive"))
            .create()
            .addCompadata(5)
            .addCompadata(6)
            .addCompadata(7)
            .addCompadata(8)
            .get();

        final var fakeStorageLocator = fakeStorageLocator(List.of(
            new StorageDrive("0", storeDrive1.root()),
            new StorageDrive("1", storeDrive2.root())
        ));
        final var actionRunner = new GetCompatdatas(fakeStorageLocator, it -> fakeAppsRepository(apps));

        actionRunner.run();

        final String output = this.output.getOutput();
        final String[] lines = output.split("\n");
        assertThat(lines[0]).isEmpty();
        assertThat(lines[1]).matches("= Library /tmp/game-tools-\\d+");
        assertThat(lines[2]).startsWith("1          Game-01");
        assertThat(lines[3]).startsWith("2          (uninstalled)   0 MB");
        assertThat(lines[4]).startsWith("3          (uninstalled)   0 MB");
        assertThat(lines[5]).startsWith("4          Game-04");
        assertThat(lines[6]).isEmpty();
        assertThat(lines[7]).matches("= Library /tmp/game-tools-\\d+");
        assertThat(lines[8]).startsWith("5          Game-05");
        assertThat(lines[9]).startsWith("6          (uninstalled)   0 MB");
        assertThat(lines[10]).startsWith("7          (uninstalled)   0 MB");
        assertThat(lines[11]).startsWith("8          Game-08");
        assertThat(lines).hasSize(12);
    }

    @Test
    void should_return_compdata_with_size() {
        final List<App> apps = List.of();
        final var storeDrive1 = new TestLibraryTemplate(TestResource.fromFile("steam-drive"))
            .create()
            .addCompadata(1, 2)
            .addCompadata(2, 3)
            .get();

        final var fakeStorageLocator = fakeStorageLocator(List.of(
            new StorageDrive("0", storeDrive1.root())
        ));
        final var actionRunner = new GetCompatdatas(fakeStorageLocator, it -> fakeAppsRepository(apps));
        actionRunner.run();

        final String output = this.output.getOutput();
        final String[] lines = output.split("\n");
        assertThat(lines[0]).isEmpty();
        assertThat(lines[1]).matches("= Library /tmp/game-tools-\\d+");
        assertThat(lines[2]).startsWith("1          (uninstalled)   2 MB");
        assertThat(lines[3]).startsWith("2          (uninstalled)   3 MB");
        assertThat(lines).hasSize(4);
    }

    @Test
    void should_return_zero_libraries() {
        final List<App> apps = List.of();
        final var fakeStorageLocator = fakeStorageLocator(List.of());
        final var actionRunner = new GetCompatdatas(fakeStorageLocator, it -> fakeAppsRepository(apps));

        actionRunner.run();

        String output = this.output.getOutput();

        assertThat(output).isEqualTo("""
            No libraries found
            """);
    }

    @Test
    void should_return_zero_compatdata() {
        final List<App> apps = List.of();
        final var storeDrive1 = new TestLibraryTemplate(TestResource.fromFile("steam-drive")).create().get();
        final var fakeStorageLocator = fakeStorageLocator(List.of(
            new StorageDrive("0", storeDrive1.root())
        ));
        final var actionRunner = new GetCompatdatas(fakeStorageLocator, it -> fakeAppsRepository(apps));

        actionRunner.run();

        final String output = this.output.getOutput();
        final String[] lines = output.split("\n");
        assertThat(lines[0]).isEmpty();
        assertThat(lines[1]).matches("= Library /tmp/game-tools-\\d+");
        assertThat(lines).hasSize(2);
    }

}
