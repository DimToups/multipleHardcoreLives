package org.mhl.multiplehardcorelives.model.gameLogic;

import java.util.UUID;

public class Player {
    /**
     * The player's UUID from its minecraft account.
     */
    private final UUID uuid;
    /**
     * The player's name.
     */
    private final String name;
    /**
     * The player's number of lives
     */
    private int lives;

    /**
     * Initialises a new Player by defining its UUID, name and its number of lives.
     * @param uuid    The player's UUID from its minecraft account.
     * @param name    The player's name.
     * @param nbLives The player's number of lives
     */
    public Player(UUID uuid, String name, int nbLives){
        this.uuid = uuid;
        this.name = name;
        this.lives = nbLives;
    }

    /**
     * Sends the player's UUID.
     * @return The player's UUID.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sends the player's name.
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sends the player's number of lives.
     * @return The player's number of lives.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Sets the number of lives of the player.
     * @param lives The number of lives of the player.
     */
    public void setNbLives(int lives) {
        this.lives = lives;
    }
}
