package org.mhl.multiplehardcorelives.view.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.session.SessionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * A TabCompleter class for the command session
 */
public class SessionTabCompleter implements TabCompleter {
    private final MhlController controller;

    public SessionTabCompleter(MhlController controller) {
        this.controller = controller;
    }

    /**
     * Recommends options to the player when typing the command session
     * @param commandSender The command sender
     * @param command The command
     * @param s The full sent command
     * @param strings The parameters sent from the command
     * @return The list of possible options at a precise index
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> args = new ArrayList<>();
        if(strings.length == 1){
            args.add("end");
            args.add("start");
            args.add("events");
        }
        if(strings.length == 2){
            if(strings[0].equals("events"))
                args.add("claimEvent");
        }
        if(strings.length == 3){
            if(strings[1].equals("claim") && controller.getCurrentSession() != null && controller.getCurrentSession().getEvents() != null)
                for(SessionEvent event : controller.getCurrentSession().getEvents())
                    args.add(String.valueOf(event.eventId));
        }
        return args;
    }
}
