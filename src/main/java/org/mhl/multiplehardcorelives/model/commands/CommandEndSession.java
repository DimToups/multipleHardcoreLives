package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

public class CommandEndSession implements CommandExecutor {
    private final MhlController controller;
    public CommandEndSession(MhlController controller){
        this.controller = controller;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO ,"Session end command has been detected. Sending request to end the session.");
        this.controller.endSession();

        return true;
    }
}
