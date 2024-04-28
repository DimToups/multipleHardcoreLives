package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

public class CommandWriteChanges extends MhlCommand {
    public CommandWriteChanges(MhlController controller){
        super(controller);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO ,"Write changes command has been detected. Sending request to write changes in the database.");
        this.controller.writeChanges();

        return true;
    }
}
