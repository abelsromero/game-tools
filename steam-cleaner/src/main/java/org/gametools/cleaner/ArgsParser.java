package org.gametools.cleaner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.Parameter;
import org.gametools.cleaner.actions.Action;

import java.util.Map;

class ArgsParser {

    private final JCommander jc = new JCommander();

    private final GenericOptions genericOptions = new GenericOptions();
    private final IdOption idOption = new IdOption();

    static class GenericOptions {
        @Parameter(names = {"-h", "--help"}, help = true)
        public boolean help;
    }

    static class IdOption extends GenericOptions {
        @Parameter
        private String id;
    }

    ArgsParser() {

        jc.addCommand("get", genericOptions);

        final Map<String, JCommander> commands = jc.getCommands();
        final JCommander get = commands.get("get");

        get.addCommand("library", idOption, "libraries");
        get.addCommand("app", idOption, "apps");
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

            final String subCommand = jc.getCommands().get(parsedCommand).getParsedCommand();
            if (subCommand == null) {
                return null;
            }

            return new Action(parsedCommand, subCommand, idOption.id);
        } catch (MissingCommandException e) {
            return null;
        }
    }

    void printUsage() {
        jc.usage();
    }
}
