package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

/**
 * A command class having the logic of the command playerInformations.
 */
public class CommandPlayerInformations extends MhlCommand {
    /**
     * Initialise a CommandPlayerInformations by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public CommandPlayerInformations(MhlController controller){
        super(controller);
    }

    /**
     * Detects the command playerInformations and asks the controller to handle it.
     * @param commandSender Source of the command
     * @param command       Command which was executed
     * @param s             Alias of the command which was used
     * @param strings       Passed command arguments
     * @return              True if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO ,"Player informations command has been detected. Sending request to get the informations of the player.");
        if(strings.length != 1){
            commandSender.sendMessage(command.getUsage());
            return false;
        }

        this.controller.displayPlayerInformations(commandSender, strings[0]);

        return true;
    }
}
