package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

/**
 * A command class having the logic of the command server.
 */
public class CommandServer extends MhlCommand {
    /**
     * Initialise a CommandServer by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public CommandServer(MhlController controller) {
        super(controller);
    }

    /**
     * Detects the command mhlServer and asks the controller to handle it.
     * @param commandSender Source of the command
     * @param command       Command which was executed
     * @param s             Alias of the command which was used
     * @param strings       Passed command arguments
     * @return              True if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO, "Command mhlServer has been detected. Sending request to handle it");
        if(strings.length != 0) {
            switch (strings[0]) {
                case "reset":
                    controller.resetServer();
                    return true;
                case "defaultNumberOfLives": {
                    if (strings.length == 2) {
                        int numberOfLives;
                        try {
                            numberOfLives = Integer.parseInt(strings[1]);
                        } catch (Exception e) {
                            commandSender.sendMessage("\"" + strings[1] + "\" is not a number");
                            return false;
                        }
                        controller.setDefaultNumberOfLives(numberOfLives);
                        return true;
                    }
                    controller.displayDefaultNumberOfLives(commandSender);
                    return true;
                }
                default:
                    return false;
            }
        }
        return false;
    }
}
