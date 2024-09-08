package org.mhl.multiplehardcorelives.model.gameModes.classic;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.mhl.multiplehardcorelives.model.gameModes.MhlGameMode;
import org.mhl.multiplehardcorelives.model.gameModes.enums.GameModes;
import org.mhl.multiplehardcorelives.model.lifeToken.NumericLifeToken;

public class Classic extends MhlGameMode {
    public Classic(){
        super(new NumericLifeToken(), new NumericLifeToken(5), new NumericLifeToken(1), GameModes.Classic);
    }

    @Override
    public void onSessionStart() {

    }

    @Override
    public void onSessionEnd() {

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent pde) {

    }
}
