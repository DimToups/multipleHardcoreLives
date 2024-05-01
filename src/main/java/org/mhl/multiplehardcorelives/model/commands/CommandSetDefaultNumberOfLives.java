package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

/**
 * A command class having the logic of the command setDefaultNumberOfLives.
 */
public class CommandSetDefaultNumberOfLives extends MhlCommand {
    /**
     * Initialise a CommandSetDefaultNumberOfLives by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public CommandSetDefaultNumberOfLives(MhlController controller){
        super(controller);
    }

    /**
     * Detects the command setDefaultNumberOfLives and asks the controller to handle it.
     * @param commandSender Source of the command
     * @param command       Command which was executed
     * @param s             Alias of the command which was used
     * @param strings       Passed command arguments
     * @return              True if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO ,"Default number of lives setter command has been detected");

        //
        if(strings.length != 1){
            commandSender.sendMessage(command.getUsage());
            return false;
        }
        //
        int defaultNumberOfLives;
        try{
            defaultNumberOfLives = Integer.parseInt(strings[0]);
        } catch (Exception e){
            commandSender.sendMessage("\"" + strings[0] + "\" is not a number");
            return false;
        }

        //
        this.controller.setDefaultNumberOfLives(defaultNumberOfLives);

        return true;
    }
}
