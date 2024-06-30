package org.mhl.multiplehardcorelives.model.commands.session;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class SessionTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> args = new ArrayList<>();
        if(strings.length == 1){
            args.add("end");
            args.add("start");
            return args;
        }
        return args;
    }
}
