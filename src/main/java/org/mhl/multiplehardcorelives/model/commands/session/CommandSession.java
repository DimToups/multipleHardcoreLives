package org.mhl.multiplehardcorelives.model.commands.session;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.commands.MhlCommand;

import java.util.logging.Level;

/**
 * A command class having the logic of the command session.
 */
public class CommandSession extends MhlCommand {
    /**
     * Initialise a CommandSession by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public CommandSession(MhlController controller) {
        super(controller);
    }

    /**
     * Detects the command session and asks the controller to handle it.
     * @param commandSender Source of the command
     * @param command       Command which was executed
     * @param s             Alias of the command which was used
     * @param strings       Passed command arguments
     * @return              True if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length != 1){
            commandSender.sendMessage("\"" + s + "\" does not have the correct amount of parameters");
            return false;
        }
        Bukkit.getLogger().log(Level.INFO ,"Session " + strings[0] + " command has been detected. Sending request to handle it");
        switch (strings[0]){
            case "start" :
                this.controller.startSession();
                break;
            case "end" :
                this.controller.endSession();
                break;
            default:
                commandSender.sendMessage("\"" + strings[0] + "\" is not a valid argument\nOnly \"start\" and \"end\" are allowed");
                return false;
        }
        return true;
    }
}
