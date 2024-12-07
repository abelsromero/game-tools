package org.gametools.cleaner.actions;


public record Action(Command command, SubCommand subCommand, boolean help, String instanceId) {

    public Action(String command, String subCommand, boolean help, String instanceId) {
        this(command == null ? null : Command.valueOf(command.toUpperCase()),
            subCommand == null ? null : SubCommand.valueOf(subCommand.toUpperCase()),
            help,
            instanceId);
    }

}
