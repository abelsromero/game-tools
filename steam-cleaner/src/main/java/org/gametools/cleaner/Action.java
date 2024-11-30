package org.gametools.cleaner;

import java.util.Objects;

public final class Action {

    private final String command;
    private final String subCommand;

    public Action(String command, String subCommand) {
        this.command = command;
        this.subCommand = subCommand;
    }

    public String command() {
        return command;
    }

    public String subCommand() {
        return subCommand;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Action) obj;
        return Objects.equals(this.command, that.command) &&
            Objects.equals(this.subCommand, that.subCommand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, subCommand);
    }

}
