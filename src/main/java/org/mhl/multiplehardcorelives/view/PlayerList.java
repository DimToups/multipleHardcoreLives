package org.mhl.multiplehardcorelives.view;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.gameLogic.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class PlayerList {
    private final MhlController controller;
    private int longestNameEntry = 0;
    public PlayerList(MhlController controller){
        this.controller = controller;
    }
    public void updatePlayerNumberOfLives(Player player) {
        if(getNameEntrySize(player) > longestNameEntry) {
            Bukkit.getLogger().log(Level.INFO, "Player " + player.getName() + " has now the biggest name entry. Re-rendering the scoreboard");
            this.updatePlayerList();
        }
        else {
            Objects.requireNonNull(Bukkit.getPlayer(player.getUuid())).setPlayerListName(this.buildPlayerListName(player));
            Bukkit.getLogger().log(Level.INFO, "Player " + player.getName() + " has now been updated in the scoreboard");
        }
    }
    public void updatePlayerList(){
        Bukkit.getLogger().log(Level.INFO, "Updating the scoreboard");
        Collection<? extends org.bukkit.entity.Player> players = Bukkit.getOnlinePlayers();
        List<Player> mhlPlayers = new ArrayList<>();

        for(org.bukkit.entity.Player player : players) {
            Player foundPlayer = controller.findPlayerSafelyByUUID(player.getUniqueId());
            mhlPlayers.add(foundPlayer);

            if(getNameEntrySize(foundPlayer) > longestNameEntry)
                longestNameEntry = foundPlayer.getName().length();
        }

        for(Player p : mhlPlayers){
            org.bukkit.entity.Player player = Bukkit.getPlayer(p.getUuid());

            player.setPlayerListName(this.buildPlayerListName(p));
            Bukkit.getLogger().log(Level.INFO, "Player " + player.getName() + " is now registered in the scoreboard");
        }
        Bukkit.getLogger().log(Level.INFO, "The scoreboard has been updated");
    }

    private String buildPlayerListName(Player player){
        StringBuilder playerListName = new StringBuilder(player.getName());
        for(int i = getNameEntrySize(player); i < longestNameEntry; i++)
            playerListName.append(" ");
        playerListName.append("  ").append(player.getLives());
        return playerListName.toString();
    }
    private int getNameEntrySize(Player player){
        if(player.getLives() == 0)
            return player.getName().length();
        return player.getName().length() + (int)(Math.log(player.getLives()));
    }
}
