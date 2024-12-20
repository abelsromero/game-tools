package org.gametools.cleaner;

import org.gametools.cleaner.actions.OutputHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prototype for integration test
 */
@Disabled
class MainTest {

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
    void run_main() {
        Runner runner = new Runner();

        runner.main(new String[]{"get", "apps", "4500"});

        String output = this.output.getOutput();
        assertThat(output).isEqualTo("""
            Command read: GET,APP
            
            
            Id:         4500
            Name:       Stonehearth
            Location:   {placeholder}/steam/steamapps/common/STALKER Shadow of Chernobyl
            Compatdata: {placeholder}/steam/steamapps/compatdata/4500
            """);
    }
}
