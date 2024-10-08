package org.mhl.multiplehardcorelives.model;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

/**
 * Listens to every useful event related to player and sends it to its controller.
 */
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
        controller.playerJoin(pje.getPlayer());
    }

    /**
     * Detects when a player quits the server. It will tell the controller to handle the situation.
     * @param pje The event of a player joining
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pje){
        Bukkit.getLogger().log(Level.INFO, "The player " + pje.getPlayer().getName() + " has quit");
        controller.playerQuit(pje.getPlayer());
    }

    /**
     * Detects when a player died. It will tell the controller to handle the situation.
     * @param pde The event of a player's death.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde){
        controller.playerDeath(pde);
    }

    /**
     * Detects when a player has done an advancement. It will tell the controller to handle the situation
     * @param pade The event of the player's advancement
     */
    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent pade){
        controller.playerAdvancementDone(pade);
    }
}
