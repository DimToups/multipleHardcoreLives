package org.mhl.multiplehardcorelives.model.gameModes;

import org.mhl.multiplehardcorelives.model.gameModes.enums.GameModes;
import org.mhl.multiplehardcorelives.model.lifeToken.LifeToken;

public abstract class MhlGameMode {

    protected final LifeToken lifeCurrency;

    protected LifeToken defaultDeathPenalty;

    protected LifeToken defaultNbLifeTokens;

    protected GameModes gameMode;

    public MhlGameMode(LifeToken lifeCurrency, LifeToken defaultNbLifeTokens, LifeToken defaultSanctionForDeath, GameModes gameMode){
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
}
