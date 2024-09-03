package org.mhl.multiplehardcorelives.model.gameModes.enums;

import org.mhl.multiplehardcorelives.model.gameModes.Classic;
import org.mhl.multiplehardcorelives.model.gameModes.MhlGameMode;
import org.mhl.multiplehardcorelives.model.lifeToken.LifeTokens;

public enum GameModes {
    Classic("Classic", "Description", LifeTokens.NumericLifeToken),
    ;
    private final String name;
    private final String description;
    private final LifeTokens numericLifeToken;

    GameModes(String name, String description, LifeTokens numericLifeToken){
        this.name = name;
        this.description = description;
        this.numericLifeToken = numericLifeToken;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static MhlGameMode toMhlGameMode(GameModes gameModes){
        return switch (gameModes){
            default -> new Classic();
        };
    }

    public LifeTokens getLifeCurrency() {
        return this.numericLifeToken;
    }

    public LifeTokens getNumericLifeToken() {
        return numericLifeToken;
    }
}
