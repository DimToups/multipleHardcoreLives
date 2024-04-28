package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.gameLogic.Player;

import java.util.logging.Level;

public class CommandSetNumberOfLivesToPlayer extends MhlCommand {
    public CommandSetNumberOfLivesToPlayer(MhlController controller) {
        super(controller);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO, "Set number of lives to player command has been detected. Sending request to changes its number of lives.");


        //
        if(strings.length != 2){
            commandSender.sendMessage(command.getUsage());
            return false;
        }
        //
        int numberOfLives;
        Player player = controller.findPlayer(strings[0]);
        if(player == null){
            commandSender.sendMessage("Player \"" + strings[0] + "\" has not been found. He may did not connect to the server yet.");
            if ((player = controller.findPlayerInDatabase(strings[0])) == null)
                return false;
        }
        try{
            numberOfLives = Integer.parseInt(strings[1]);
        } catch (Exception e){
            commandSender.sendMessage("\"" + strings[1] + "\" is not a number");
            return false;
        }

        controller.addPlayer(player);

        controller.setNbLivesOfPlayer(player, numberOfLives);

        return true;
    }
}
