package org.mhl.multiplehardcorelives.model;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mhl.multiplehardcorelives.controller.MhlController;

import java.util.logging.Level;

public class PlayerListener implements Listener {
    private final MhlController controller;
    public PlayerListener(MhlController controller){
        this.controller = controller;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje){
        Bukkit.getLogger().log(Level.INFO, "The player " + pje.getPlayer().getName() + " has joined");
        controller.addPlayer(pje.getPlayer());
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde){
        controller.decrementLivesOfPlayer(pde.getEntity());
    }
}
