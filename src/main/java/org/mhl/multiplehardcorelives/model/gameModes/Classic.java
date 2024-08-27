package org.mhl.multiplehardcorelives.model.gameModes;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.mhl.multiplehardcorelives.model.gameModes.enums.GameModes;
import org.mhl.multiplehardcorelives.model.lifeToken.LifeToken;
import org.mhl.multiplehardcorelives.model.lifeToken.NumericLifeToken;

public class Classic extends MhlGameMode {
    public Classic(){
        super(new NumericLifeToken(), new NumericLifeToken(1), GameModes.Classic);
    }

    @Override
    public LifeToken getDeathPenalty() {
        return null;
    }

    @Override
    public void playerDeath(PlayerDeathEvent deathEvent) {

    }
}
