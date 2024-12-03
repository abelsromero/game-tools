package org.gametools.cleaner.actions;


public record Action(Command command, SubCommand subCommand) {

    public Action(String command, String subCommand) {
        this(Command.valueOf(command.toUpperCase()), SubCommand.valueOf(subCommand.toUpperCase()));
    }

}
