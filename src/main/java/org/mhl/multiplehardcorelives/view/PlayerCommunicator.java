package org.mhl.multiplehardcorelives.view;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerCommunicator {

    public void informPlayerDefinitiveDeath(Player player){
        player.sendTitle("You are out of lives !", "", 0, 70, 40);
    }

    public void tellSessionStart() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "The session has just started");
    }

    public void tellSessionNearlyEnded() {
        Bukkit.broadcastMessage(ChatColor.RED + "The session is ending in 30 seconds");
    }

    public void tellSessionEnd() {
        Bukkit.broadcastMessage(ChatColor.RED + "The session has now ended");
    }

    public void tellTimeLeft(int seconds) {
        Bukkit.broadcastMessage(ChatColor.RED + "The session is ending in " + seconds + " seconds");
    }

    public void tellToEveryPlayer(String msg) {
        Bukkit.broadcastMessage(msg);
    }
}
