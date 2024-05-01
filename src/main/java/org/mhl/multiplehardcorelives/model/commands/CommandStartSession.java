package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

public class CommandStartSession extends MhlCommand {
    /**
     * Initialise a CommandStartSession by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public CommandStartSession(MhlController controller){
        super(controller);
    }

    /**
     * Detects the command startSession and asks the controller to handle it.
     * @param commandSender Source of the command
     * @param command       Command which was executed
     * @param s             Alias of the command which was used
     * @param strings       Passed command arguments
     * @return              True if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO ,"Session start command has been detected. Sending request to start the session.");
        this.controller.startSession();

        return true;
    }
}
