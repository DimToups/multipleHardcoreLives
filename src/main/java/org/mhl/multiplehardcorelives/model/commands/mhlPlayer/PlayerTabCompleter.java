package org.mhl.multiplehardcorelives.model.commands.mhlPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 1){
            List<String> args = new ArrayList<>();
            args.add("list");
            args.add("infos");
            args.add("setLives");
            return args;
        } else if (strings.length == 2 && (strings[0].equals("infos") || strings[0].equals("setLives"))) {
            List<String> args = new ArrayList<>();
            for(Player player : Bukkit.getOnlinePlayers())
                args.add(player.getName());
            return args;
        }

        List<String> args = new ArrayList<>();
        args.add("");
        return args;
    }
}
