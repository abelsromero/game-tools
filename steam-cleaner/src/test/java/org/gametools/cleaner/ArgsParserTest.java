package org.gametools.cleaner;

import org.gametools.cleaner.actions.Action;
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
    void should_parse_subcommands(String[] args, Action expectedAction) {
        Action action = argsParser.parse(args);

        assertThat(action).isEqualTo(expectedAction);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-h", "--help"})
    void should_parse_help_command(String help) {
        final String[] args = new String[]{"get", help, "apps", "123"};
        Action action = argsParser.parse(args);

        assertThat(action).isEqualTo(new Action("get", null, true, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-h", "--help"})
    void should_parse_help_subcommand(String help) {
        final String[] args = new String[]{"get", "apps", help, "123"};
        Action action = argsParser.parse(args);

        assertThat(action).isEqualTo(new Action("get", "app", true, null));
    }

    private static Stream<Arguments> validCommands() {
        return Stream.of(
            Arguments.of(toArray("get libraries"), action("get", "library")),
            Arguments.of(toArray("get library"), action("get", "library")),
            Arguments.of(toArray("get apps"), action("get", "app")),
            Arguments.of(toArray("get app"), action("get", "app")),
            Arguments.of(toArray("get app 123"), action("get", "app", "123")),
            Arguments.of(toArray("get library 123"), action("get", "library", "123")),
            Arguments.of(toArray("get apps 123"), action("get", "app", "123")),
            Arguments.of(toArray("get libraries 123"), action("get", "library", "123"))
        );
    }

    private static Action action(String com, String subcom) {
        return action(com, subcom, null);
    }

    private static Action action(String com, String subcom, String id) {
        return new Action(com, subcom, false, id);
    }

    private static String[] toArray(String command) {
        return command.split(" ");
    }

}
