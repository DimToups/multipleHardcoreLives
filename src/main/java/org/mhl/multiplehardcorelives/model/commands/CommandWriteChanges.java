package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

public class CommandWriteChanges extends MhlCommand {
    /**
     * Initialise a CommandWriteChanges by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public CommandWriteChanges(MhlController controller){
        super(controller);
    }

    /**
     * Detects the command writeChanges and asks the controller to handle it.
     * @param commandSender Source of the command
     * @param command       Command which was executed
     * @param s             Alias of the command which was used
     * @param strings       Passed command arguments
     * @return              True if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO ,"Write changes command has been detected. Sending request to write changes in the database.");
        this.controller.writeChanges();

        return true;
    }
}
