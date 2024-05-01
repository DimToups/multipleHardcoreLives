package org.mhl.multiplehardcorelives.model;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

public class PlayerListener implements Listener {
    /**
     * The plugin's controller. It will handle events that this class receives.
     */
    private final MhlController controller;

    /**
     * Initialise the PlayerListener. This constructor links the listener to the main MhlController instance of the plugin.
     * @param controller The main MhlController instance of the plugin.
     */
    public PlayerListener(MhlController controller){
        this.controller = controller;
    }

    /**
     * Detects when a player join the server. It will log the event and tell the controller to handle the situation.
     * @param pje The event of a player joining.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje){
        Bukkit.getLogger().log(Level.INFO, "The player " + pje.getPlayer().getName() + " has joined");
        controller.addPlayer(pje.getPlayer());
    }

    /**
     * Detects when a player died. It will tell the controller to handle the situation.
     * @param pde The event of a player's death.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde){
        controller.decrementLivesOfPlayer(pde.getEntity());
    }
}
