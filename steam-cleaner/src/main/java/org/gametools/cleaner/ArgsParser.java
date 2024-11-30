package org.gametools.cleaner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.Parameter;

import java.util.Map;

class ArgsParser {

    private final JCommander jc = new JCommander();

    static class GenericOptions {
        @Parameter(names = {"-h", "--help"}, help = true)
        public boolean help;

//        @Parameter(names = {"version"})
//        public boolean version;
    }


    ArgsParser() {
        jc.addCommand("get", new GenericOptions());

        final Map<String, JCommander> commands = jc.getCommands();
        final JCommander get = commands.get("get");
        get.addCommand("library", new GenericOptions(), "libraries");
        get.addCommand("app", new GenericOptions(), "apps");
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

            return new Action(parsedCommand, subCommand);
        } catch (MissingCommandException e) {
            return null;
        }
    }

    void printUsage() {
        jc.usage();
    }
}
