package org.mhl.multiplehardcorelives.model.gameModes;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.mhl.multiplehardcorelives.model.gameModes.enums.GameModes;
import org.mhl.multiplehardcorelives.model.lifeToken.LifeToken;

public abstract class MhlGameMode {

    protected final LifeToken lifeToken;

    protected LifeToken defaultDeathPenalty;

    protected GameModes gameMode;

    public MhlGameMode(LifeToken lifeCurrency, LifeToken defaultSanctionForDeath, GameModes gameMode){
        this.lifeToken = lifeCurrency;
        this.defaultDeathPenalty = defaultSanctionForDeath;
        this.gameMode = gameMode;
    }

    public abstract LifeToken getDeathPenalty();

    public abstract void playerDeath(PlayerDeathEvent deathEvent);

    public GameModes getGameMode(){
        return this.gameMode;
    }
}
