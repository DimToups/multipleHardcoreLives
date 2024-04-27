package org.mhl.multiplehardcorelives.model.gameLogic;

import java.util.UUID;

public class Player {
    private final UUID uuid;
    private final String name;
    private int lives;
    public Player(UUID uuid, String name, int nbLives){
        this.uuid = uuid;
        this.name = name;
        this.lives = nbLives;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getLives() {
        return lives;
    }

    public void setNbLives(int lives) {
        this.lives = lives;
    }
}
