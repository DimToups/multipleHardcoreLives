package org.mhl.multiplehardcorelives.view;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

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

    public void tellWhoIsImposter(List<org.mhl.multiplehardcorelives.model.gameLogic.Player> players, org.mhl.multiplehardcorelives.model.gameLogic.Player impostor) {
        Bukkit.getLogger().log(Level.INFO, "Telling players their role");

        Bukkit.broadcastMessage(ChatColor.RED + "The imposter is about to be chosen");
        try{
            TimeUnit.SECONDS.sleep(10);

            for(org.mhl.multiplehardcorelives.model.gameLogic.Player player : players)
                Bukkit.getPlayer(player.getUuid()).sendTitle("You are...", "", 20 / 2, 20 * 2, 20 /2);

            TimeUnit.SECONDS.sleep(5);

            for(org.mhl.multiplehardcorelives.model.gameLogic.Player player : players){
                if(player.getUuid() == impostor.getUuid())
                    Bukkit.getPlayer(player.getUuid()).sendTitle(ChatColor.RED + "Impostor", ChatColor.RED + "Kill at least one player once", 20, 20 * 3, 20 * 2);
                else
                    Bukkit.getPlayer(player.getUuid()).sendTitle("Not the impostor", "Play as usual...", 20, 20 * 3, 20 * 2);
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not tell the players who is the impostor\n" + e);
        }
    }
}
