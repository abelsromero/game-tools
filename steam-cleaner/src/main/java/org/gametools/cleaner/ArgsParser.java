package org.gametools.cleaner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.Parameter;
import org.gametools.cleaner.actions.Action;
import org.gametools.cleaner.actions.SubCommand;

import java.util.Locale;
import java.util.Map;

import static org.gametools.cleaner.actions.SubCommand.*;

class ArgsParser {

    private final JCommander jc = new JCommander();

    private final GenericOptions commandOptions = new GenericOptions();
    private final IdOption subcommandOptions = new IdOption();

    static class GenericOptions {
        @Parameter(names = {"-h", "--help"}, help = true)
        public boolean help;
    }

    static class IdOption extends GenericOptions {
        @Parameter
        private String id;
    }

    ArgsParser() {

        jc.addCommand("get", commandOptions);

        final Map<String, JCommander> commands = jc.getCommands();
        final JCommander get = commands.get("get");

        get.addCommand(name(LIBRARY), subcommandOptions, "libraries");
        get.addCommand(name(APP), subcommandOptions, "apps");
        get.addCommand(name(COMPATDATA), subcommandOptions, "compatdatas");
    }

    private static String name(SubCommand subCommand) {
        return subCommand.name().toLowerCase(Locale.ENGLISH);
    }

    /**
     * Returns Action to handle arguments, or null if no action could be matched.
     */
    Action parse(String[] args) {
        try {
            jc.parse(args);

            final String parsedCommand = jc.getParsedCommand();
            if (parsedCommand == null) {
                return null;
            }
            if (commandOptions.help) {
                return new Action(parsedCommand, null, true, null);
            }

            final String subCommand = jc.getCommands().get(parsedCommand).getParsedCommand();
            if (subCommand == null) {
                return null;
            }
            if (subcommandOptions.help) {
                return new Action(parsedCommand, subCommand, true, null);
            }

            return new Action(parsedCommand, subCommand, false, subcommandOptions.id);
        } catch (MissingCommandException e) {
            return null;
        }
    }

    void printUsage() {
        jc.usage();
    }
}
