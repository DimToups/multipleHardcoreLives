package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.gameLogic.Player;

import java.util.logging.Level;

/**
 * A command class having the logic of the command player.
 */
public class CommandPlayer extends MhlCommand {
    /**
     * Initialise a CommandPlayer by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public CommandPlayer(MhlController controller) {
        super(controller);
    }

    /**
     * Detects the command mhlPlayer and asks the controller to handle it.
     * @param commandSender Source of the command
     * @param command       Command which was executed
     * @param s             Alias of the command which was used
     * @param strings       Passed command arguments
     * @return              True if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length > 0){
            switch (strings[0]){
                case "list" :  this.controller.displayPlayerList(commandSender); return true;
                case "infos" : {
                    if(strings.length > 1){
                        this.controller.displayPlayerInformations(commandSender, strings[1]);
                        return true;
                    }
                }
                case "setLives" : {
                    if(strings.length > 2){
                        //
                        int numberOfLives;
                        Player player = controller.findPlayerSafelyByName(strings[1]);

                        if(player == null){
                            commandSender.sendMessage("Player \"" + strings[1] + "\" has not been found. He may did not connect to the server yet.");
                            return false;
                        }

                        try{
                            numberOfLives = Integer.parseInt(strings[2]);
                        } catch (Exception e){
                            commandSender.sendMessage("\"" + strings[2] + "\" is not a number");
                            return false;
                        }

                        controller.addPlayer(player);
                        controller.setNbLivesOfPlayer(player, numberOfLives);

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
