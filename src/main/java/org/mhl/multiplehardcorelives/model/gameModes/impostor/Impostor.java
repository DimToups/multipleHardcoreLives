package org.mhl.multiplehardcorelives.model.gameModes.impostor;

import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.gameLogic.Player;
import org.mhl.multiplehardcorelives.model.gameModes.MhlGameMode;
import org.mhl.multiplehardcorelives.model.gameModes.enums.GameModes;
import org.mhl.multiplehardcorelives.model.lifeToken.NumericLifeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;

public class Impostor extends MhlGameMode {
    private Player impostor;
    private boolean hasImposterKilled = false;

    public Impostor(MhlController controller) {
        super(controller, new NumericLifeToken(), new NumericLifeToken(5), new NumericLifeToken(1), GameModes.Impostor);
    }

    public void onSessionStart() {
        List<Player> players = new ArrayList<>();
        for(Player player : controller.getServer().getOnlinePlayers())
            if(player.getLivesTokens().isSuperior(new NumericLifeToken(1)))
                players.add(player);
        if(players.isEmpty())
            players = controller.getServer().getOnlinePlayers();
        Random rand = new Random();
        this.impostor = players.get(rand.nextInt(players.size()));
        Bukkit.getLogger().log(Level.INFO, "The impostor for the session is " + impostor.getName());
    }

    public void onPlayerDeath(PlayerDeathEvent pde) {
        if(Objects.requireNonNull(pde.getDeathMessage()).contains(impostor.getName()) && !pde.getEntity().getUniqueId().equals(impostor.getUuid())){
            hasImposterKilled = true;
            Bukkit.getLogger().log(Level.INFO, "The imposter has killed someone");
        }
    }

    public void onSessionEnd() {
        if (!hasImposterKilled)
            controller.setNbLivesOfPlayer(impostor, new NumericLifeToken(1));
    }
}
