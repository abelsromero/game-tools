package org.gametools.cleaner.actions;


public record Action(Command command, SubCommand subCommand, String instanceId) {

    public Action(String command, String subCommand, String instanceId) {
        this(Command.valueOf(command.toUpperCase()), SubCommand.valueOf(subCommand.toUpperCase()), instanceId);
    }
}
