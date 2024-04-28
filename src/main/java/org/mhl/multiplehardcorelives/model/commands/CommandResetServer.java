package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;

public class CommandResetServer extends MhlCommand {
    public CommandResetServer(MhlController controller) {
        super(controller);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        controller.resetServer();
        return true;
    }
}
