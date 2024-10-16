package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.session.SessionEvent;

import java.util.logging.Level;

/**
 * A command class having the logic of the command session.
 */
public class CommandSession extends MhlCommand {
    /**
     * Initialise a CommandSession by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public CommandSession(MhlController controller) {
        super(controller);
    }

    /**
     * Detects the command session and asks the controller to handle it.
     * @param commandSender Source of the command
     * @param command       Command which was executed
     * @param s             Alias of the command which was used
     * @param strings       Passed command arguments
     * @return              True if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getLogger().log(Level.INFO ,"Session " + strings[0] + " command has been detected. Sending request to handle it");
        if(strings.length == 1){
            switch (strings[0]){
                case "start" : this.controller.startSession(); break;
                case "end" : this.controller.endSession(); break;
                case "events" : {
                    if (controller.getCurrentSession() != null)
                        for (SessionEvent event : controller.getCurrentSession().getEvents())
                            commandSender.sendMessage(event.eventId + ": " + event.description);
                    else
                        commandSender.sendMessage("The session has not started yet");
                    break;
                }
            }
        }
        else if (strings.length == 3){
            if(strings[0].equals("events")) {
                if (strings[1].equals("claimEvent")) {
                    try{
                        this.controller.claimEvent(commandSender, Integer.parseInt(strings[2]));
                    } catch (Exception e){
                        return false;
                    }
                }
                if (strings[1].equals("revokeEventClaim"))
                    try {
                        this.controller.revokeEvent(commandSender, Integer.parseInt(strings[2]));
                    } catch (Exception e){
                        return false;
                    }
            }
        }
        else if (strings.length == 4)
            if(strings[0].equals("events"))
                if (strings[1].equals("assignDeathClaim"))
                    try {
                        this.controller.assignDeathClaim(commandSender, Integer.parseInt(strings[2]), controller.findPlayerSafelyByName(strings[3]));
                    } catch (Exception e){
                        return false;
                    }
        else
            return false;
        return true;
    }
}
