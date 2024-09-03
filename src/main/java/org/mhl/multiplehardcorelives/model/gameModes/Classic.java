package org.mhl.multiplehardcorelives.model.gameModes;

import org.mhl.multiplehardcorelives.model.gameModes.enums.GameModes;
import org.mhl.multiplehardcorelives.model.lifeToken.NumericLifeToken;

public class Classic extends MhlGameMode {
    public Classic(){
        super(new NumericLifeToken(), new NumericLifeToken(5), new NumericLifeToken(1), GameModes.Classic);
    }
}
