package org.mhl.multiplehardcorelives.view;

import org.bukkit.entity.Player;

public class PlayerCommunicator {

    public void informPlayerDefinitiveDeath(Player player){
        player.sendTitle("You are out of lives !", "", 0, 70, 40);
    }
}
