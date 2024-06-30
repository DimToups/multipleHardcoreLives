package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

/**
 * A command class having the logic of the command verify.
 */
public class CommandVerify extends MhlCommand {
    /**
     * Initialise a CommandVerify by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public CommandVerify(MhlController controller) {
        super(controller);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO, "Command mhlVerify has been detected. Sending request to handle it");
        if(strings.length > 0) {
            switch (strings[0]) {
                case "server" :
                    controller.verifyServerState();
                    return true;
                default:
                    Bukkit.getLogger().log(Level.WARNING, "Argument \"" + strings[0] + "\" is not a valid argument");
            }
        }
        return false;
    }
}
