package org.mhl.multiplehardcorelives.model.gameModes;

import org.mhl.multiplehardcorelives.model.gameModes.enums.GameModes;
import org.mhl.multiplehardcorelives.model.lifeToken.LifeToken;

public abstract class MhlGameMode {

    protected final LifeToken lifeCurrency;

    protected LifeToken defaultDeathPenalty;

    protected GameModes gameMode;

    public MhlGameMode(LifeToken lifeCurrency, LifeToken defaultSanctionForDeath, GameModes gameMode){
        this.lifeCurrency = lifeCurrency;
        this.defaultDeathPenalty = defaultSanctionForDeath;
        this.gameMode = gameMode;
    }

    public abstract LifeToken getDeathPenalty();

    public LifeToken getLifeCurrency() {
        return lifeCurrency;
    }

    public GameModes getGameMode(){
        return this.gameMode;
    }

    public abstract LifeToken getDefaultNbLifeTokens();
}
