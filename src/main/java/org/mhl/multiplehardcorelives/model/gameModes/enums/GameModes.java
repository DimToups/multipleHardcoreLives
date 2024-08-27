package org.mhl.multiplehardcorelives.model.gameModes.enums;

import org.mhl.multiplehardcorelives.model.gameModes.Classic;
import org.mhl.multiplehardcorelives.model.gameModes.MhlGameMode;

public enum GameModes {
    Classic("Classic", "Description"),
    ;
    private final String name;
    private final String description;

    GameModes(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static MhlGameMode toMhlGameMode(GameModes gameModes){
        switch (gameModes){
            default : return new Classic();
        }
    }
}
