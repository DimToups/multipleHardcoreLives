package org.mhl.multiplehardcorelives.model.commands.mhlWorldBorder;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.commands.MhlCommand;

import java.util.logging.Level;

public class CommandMhlWorldBorder extends MhlCommand {
    /**
     * Initialise a CommandMhlWorldBorder by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public CommandMhlWorldBorder(MhlController controller) {
        super(controller);
    }

    /**
     * Detects the command setNumberOfLivesToPlayer and asks the controller to handle it.
     *
     * @param commandSender Source of the command
     * @param command       Command which was executed
     * @param s             Alias of the command which was used
     * @param strings       Passed command arguments
     * @return True if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO, "Command mhlWorldBorder has been detected. Sending request to handle it");
        if(strings.length > 0) {
            switch (strings[0]) {
                case "reload": {
                    controller.reloadWorldBorder();
                    return true;
                }
                case "length": {
                    if (strings.length == 1) {
                        commandSender.sendMessage("World border has a length of " + controller.getWorldBorderLength() + " blocks");
                        return true;
                    }
                    else if (strings.length == 2){
                        int length;
                        try{
                            length = Integer.parseInt(strings[1]);
                        } catch (Exception e){
                            Bukkit.getLogger().log(Level.WARNING,"\"" + strings[1] + "\" is not a number");
                            Bukkit.getLogger().log(Level.WARNING,"The request to set the length of the world border has been canceled.");
                            commandSender.sendMessage("\"" + strings[1] + "\" is not a number");
                            return false;
                        }

                        controller.setWorldBorderLength(length);

                        return true;
                    }
                }
                default:
                    Bukkit.getLogger().log(Level.WARNING, "Argument \"" + strings[0] + "\" is not a valid argument");
            }
        }
        return false;
    }
}
