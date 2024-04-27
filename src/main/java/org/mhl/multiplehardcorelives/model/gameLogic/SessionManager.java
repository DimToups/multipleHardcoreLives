package org.mhl.multiplehardcorelives.model.gameLogic;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.PlayerListener;

import java.util.logging.Level;

public class SessionManager {
    private boolean isSessionActive = false;
    private final PlayerListener playerListener;
    private final MhlController controller;
    public SessionManager(MhlController controller){
        this.controller = controller;
        playerListener = new PlayerListener(controller);

        //
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(playerListener, this.controller.getPlugin());
        Bukkit.getLogger().log(Level.INFO, "Now listening to player informations");
    }
    public boolean isSessionActive() {
        return isSessionActive;
    }
    public void startSession(){
        //
        if(isSessionActive){
            Bukkit.getLogger().warning("Session has already started");
            return;
        }

        //
        isSessionActive = true;
    }
    public void endSession(){
        isSessionActive = false;
    }
}
