package org.gametools.cleaner.actions;

public class RunnerResolver {

    public ActionRunner resolver(Action action) {

        return switch (action.command()) {
            case GET -> switch (action.subCommand()) {
                case LIBRARY -> new GetLibraries();
                case APP -> new GetApps();
            };
            case LIST -> switch (action.subCommand()) {
                case LIBRARY, APP -> new VoidRunner();
            };
        };
    }

}
