package org.gametools.cleaner;

import org.gametools.cleaner.actions.Action;
import org.gametools.cleaner.actions.RunnerResolver;

public class Runner {

    public static void main(String[] args) {

        final ArgsParser argsParser = new ArgsParser();

        Action action = argsParser.parse(args);

        if (action == null) {
            argsParser.printUsage();
            return;
        }

        if (action != null) {
            System.out.println("Command read: %s,%s".formatted(action.command(), action.subCommand()));
            System.out.println("\n");
        }

        new RunnerResolver()
            .resolver(action)
            .run();
    }
}
