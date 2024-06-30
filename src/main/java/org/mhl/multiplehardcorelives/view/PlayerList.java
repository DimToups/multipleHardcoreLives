package org.mhl.multiplehardcorelives.view;

import org.bukkit.Bukkit;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.gameLogic.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

/**
 * A view made to replace the player list to display the number of lives of the players and to color their name.
 */
public class PlayerList {
    /**
     * The plugin's controller.
     */
    private final MhlController controller;

    /**
     * The longest possible name entry in the player list.
     * It takes the number of characters and the number of digits in their remaining number of lives.
     * Only online players are included in this field.
     */
    private int longestNameEntry = 0;

    /**
     * Creates an instance of PlayerList by setting its controller.
     * @param controller The plugin's controller.
     */
    public PlayerList(MhlController controller){
        this.controller = controller;
    }

    /**
     * Updates the number of lives of the specified player in the displayed player list.
     * If the new name entry is now the biggest one, it will update the whole player list
     * @param player The wanted player.
     */
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

    /**
     * Updates the whole player list, display every player's name and number of lives, and updates the longestNameEntry.
     */
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

    /**
     * Builds the name entry for a specified player by taking its name and number of lives, and set the correct space between these.
     * @param player The wanted player.
     * @return       The String corresponding to its nam entry.
     */
    private String buildPlayerListName(Player player){
        StringBuilder playerListName = new StringBuilder(player.getName());
        for(int i = getNameEntrySize(player); i < longestNameEntry; i++)
            playerListName.append(" ");
        playerListName.append("  ").append(player.getLives());
        return playerListName.toString();
    }

    /**
     * Sends the player's name entry size by making the sum of its name's size and the number of digits of its number of lives.
     * @param player The wanted player.
     * @return       The processed name entry's size
     */
    private int getNameEntrySize(Player player){
        if(player.getLives() == 0)
            return player.getName().length() + 1;
        return player.getName().length() + (int)(Math.log(player.getLives()));
    }
}
