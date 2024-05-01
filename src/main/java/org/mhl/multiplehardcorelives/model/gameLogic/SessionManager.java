package org.mhl.multiplehardcorelives.model.gameLogic;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.PlayerListener;

import java.util.logging.Level;

/**
 * A class that contains the logic of the plugin for its sessions.
 */
public class SessionManager {

    /**
     * A boolean indicating if the session is active or not.
     */
    private boolean isSessionActive = false;

    /**
     * The event listener about players data.
     */
    private final PlayerListener playerListener;

    /**
     * The plugin's controller.
     */
    private final MhlController controller;

    /**
     * Initialises a SessionManager that will listen to players events.
     * @param controller The plugin's controller.
     */
    public SessionManager(MhlController controller){
        this.controller = controller;
        playerListener = new PlayerListener(controller);

        //
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(playerListener, this.controller.getPlugin());
        Bukkit.getLogger().log(Level.INFO, "Now listening to player informations");
    }

    /**
     * Sends the state of the session.
     * @return The state of the session.
     */
    public boolean isSessionActive() {
        return isSessionActive;
    }

    /**
     * Starts the session by setting the field isSessionActive to true.
     */
    public void startSession(){
        //
        if(isSessionActive){
            Bukkit.getLogger().warning("Session has already started");
            return;
        }

        //
        isSessionActive = true;
    }

    /**
     * Ends the sessions by setting the field isSessionActive to false.
     */
    public void endSession(){
        if(!isSessionActive){
            Bukkit.getLogger().warning("Session has already stopped");
            return;
        }

        isSessionActive = false;
    }
}
