package org.gametools.cleaner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ArgsParserTest {

    private final ArgsParser argsParser = new ArgsParser();

    @Test
    void should_not_parse_empty_args() {
        final String[] args = new String[]{};

        Action action = argsParser.parse(args);

        assertThat(action).isNull();
    }

    @Test
    void should_not_parse_invalid_command() {
        final String[] args = new String[]{"fake-command"};

        Action action = argsParser.parse(args);

        assertThat(action).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"get"})
    void should_not_parse_incomplete_command(String command) {
        final String[] args = new String[]{command};

        Action action = argsParser.parse(args);

        assertThat(action).isNull();
    }

    @ParameterizedTest
    @MethodSource("validCommands")
    void should_parse_subcommand(String[] args, Action expectedAction) {
        Action action = argsParser.parse(args);

        assertThat(action).isEqualTo(expectedAction);
    }

    private static Stream<Arguments> validCommands() {
        return Stream.of(
            Arguments.of(toArray("get libraries"), action("get", "library")),
            Arguments.of(toArray("get library"), action("get", "library")),
            Arguments.of(toArray("get apps"), action("get", "app")),
            Arguments.of(toArray("get app"), action("get", "app"))
        );
    }

    private static Action action(String com, String subcom) {
        return new Action(com, subcom);
    }

    private static String[] toArray(String command) {
        return command.split(" ");
    }

}
