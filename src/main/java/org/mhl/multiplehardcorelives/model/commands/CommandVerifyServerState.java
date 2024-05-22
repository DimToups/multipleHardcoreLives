package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;

public class CommandVerifyServerState extends MhlCommand{
    /**
     * Initialise a CommandVerifyServerState class by using the plugin's controller.
     *
     * @param controller The plugin's controller
     */
    public CommandVerifyServerState(MhlController controller) {
        super(controller);
    }

    /**
     * Detects the command verifyServerState and asks the controller to handle it.
     * @param commandSender Source of the command
     * @param command       Command which was executed
     * @param s             Alias of the command which was used
     * @param strings       Passed command arguments
     * @return              True if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        controller.verifyServerState();
        return true;
    }
}
