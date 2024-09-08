package org.mhl.multiplehardcorelives.model.gameModes;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.gameModes.enums.GameModes;
import org.mhl.multiplehardcorelives.model.lifeToken.LifeToken;

public abstract class MhlGameMode {

    protected final LifeToken lifeCurrency;

    protected LifeToken defaultDeathPenalty;

    protected LifeToken defaultNbLifeTokens;

    protected GameModes gameMode;

    protected MhlController controller;

    public MhlGameMode(LifeToken lifeCurrency, LifeToken defaultNbLifeTokens, LifeToken defaultSanctionForDeath, GameModes gameMode){
        this.lifeCurrency = lifeCurrency;
        this.defaultNbLifeTokens = defaultNbLifeTokens;
        this.defaultDeathPenalty = defaultSanctionForDeath;
        this.gameMode = gameMode;
    }

    public MhlGameMode(MhlController controller, LifeToken lifeCurrency, LifeToken defaultNbLifeTokens, LifeToken defaultSanctionForDeath, GameModes gameMode){
        this.controller = controller;
        this.lifeCurrency = lifeCurrency;
        this.defaultNbLifeTokens = defaultNbLifeTokens;
        this.defaultDeathPenalty = defaultSanctionForDeath;
        this.gameMode = gameMode;
    }

    public LifeToken getDeathPenalty(){
        return this.defaultDeathPenalty;
    }

    public LifeToken getLifeCurrency() {
        return lifeCurrency;
    }

    public GameModes getGameMode(){
        return this.gameMode;
    }

    public LifeToken getDefaultNbLifeTokens(){
        return this.defaultNbLifeTokens;
    }


    public abstract void onSessionStart();

    public abstract void onSessionEnd();

    public abstract void onPlayerDeath(PlayerDeathEvent pde);
}
