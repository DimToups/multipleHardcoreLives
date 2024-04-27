package org.mhl.multiplehardcorelives.model.gameLogic;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Server {
    private final String address;
    private int defaultNbLives = 5;
    private final List<Player> players = new ArrayList<>();
    public Server(String address){
        this.address = address;
    }
    public Server(String address, int nbLives){
        this.address = address;
        this.defaultNbLives = nbLives;
    }
    public String getAddress(){
        return this.address;
    }
    public List<Player> getPlayers() {
        return players;
    }

    public int getDefaultNbLives() {
        return defaultNbLives;
    }

    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
        Bukkit.getLogger().log(Level.INFO, "Added the player with the UUID " + newPlayer.getUuid() + " to the controller's server instance");
    }
}
