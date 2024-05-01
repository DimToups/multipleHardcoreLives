package org.mhl.multiplehardcorelives.model.gameLogic;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * A model that stores data about the server and its players.
 */
public class Server {
    /**
     * The server's address.
     */
    private final String address;
    /**
     * The default number of lives.
     */
    private int defaultNbLives = 5;
    /**
     * The server's list of loaded players.
     */
    private final List<Player> players = new ArrayList<>();

    /**
     * Initialises a new Server by using its address.
     * @param address The server's address.
     */
    public Server(String address){
        this.address = address;
    }

    /**
     * Initialises a new Server by using its address and the wanted default number of lives.
     * @param address The server's address.
     * @param nbLives The wanted default number of lives.
     */
    public Server(String address, int nbLives){
        this.address = address;
        this.defaultNbLives = nbLives;
    }

    /**
     * Sends the server's address.
     * @return The server's address.
     */
    public String getAddress(){
        return this.address;
    }

    /**
     * Sends the server's loaded players.
     * @return The loaded players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Sends the default number of lives.
     * @return The default number of lives.
     */
    public int getDefaultNbLives() {
        return defaultNbLives;
    }

    /**
     * Sets the default number of lives.
     * @param defaultNbLives The default number of lives.
     */
    public void setDefaultNbLives(int defaultNbLives) {
        Bukkit.getLogger().log(Level.INFO, "Setting the default number of lives to " + defaultNbLives);
        this.defaultNbLives = defaultNbLives;
    }

    /**
     * Adds a new player to the list of loaded players.
     * @param newPlayer The new loaded player.
     */
    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
        Bukkit.getLogger().log(Level.INFO, "Added the player with the UUID " + newPlayer.getUuid() + " to the controller's server instance");
    }
}
