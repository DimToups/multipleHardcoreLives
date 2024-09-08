package org.mhl.multiplehardcorelives.model.gameModes.enums;

import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.gameModes.classic.Classic;
import org.mhl.multiplehardcorelives.model.gameModes.MhlGameMode;
import org.mhl.multiplehardcorelives.model.gameModes.impostor.Impostor;
import org.mhl.multiplehardcorelives.model.lifeToken.LifeTokens;

public enum GameModes {
    Classic("Classic", "Description", LifeTokens.NumericLifeToken),
    Impostor("Impostor", "Description", LifeTokens.NumericLifeToken),
    ;

    private final String name;
    private final String description;
    private final LifeTokens lifeTokens;

    GameModes(String name, String description, LifeTokens numericLifeToken){
        this.name = name;
        this.description = description;
        this.lifeTokens = numericLifeToken;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @org.jetbrains.annotations.Nullable
    public static MhlGameMode toMhlGameMode(MhlController controller, GameModes gameModes){
        return switch (gameModes){
            case Classic -> new Classic();
            case Impostor -> new Impostor(controller);
            default -> null;
        };
    }

    public LifeTokens getLifeCurrency() {
        return this.lifeTokens;
    }

    public LifeTokens getLifeTokens() {
        return lifeTokens;
    }
}
