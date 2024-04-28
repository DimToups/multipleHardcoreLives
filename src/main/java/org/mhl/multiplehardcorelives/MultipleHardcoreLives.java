package org.mhl.multiplehardcorelives;

import org.bukkit.plugin.java.JavaPlugin;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.commands.*;

public final class MultipleHardcoreLives extends JavaPlugin {
    private MhlController controller;

    @Override
    public void onEnable() {
        //
        controller = new MhlController(this);

        //
        this.loadCommands();
    }

    private void loadCommands(){
        this.getCommand("startSession").setExecutor(new CommandStartSession(controller));
        this.getCommand("endSession").setExecutor(new CommandEndSession(controller));
        this.getCommand("setDefaultNumberOfLives").setExecutor(new CommandSetDefaultNumberOfLives(controller));
        this.getCommand("writeChanges").setExecutor(new CommandWriteChanges(controller));
        this.getCommand("setNumberOfLivesToPlayer").setExecutor(new CommandSetNumberOfLivesToPlayer(controller));
    }

    @Override
    public void onDisable() {
        controller.serverClosing();
    }
}
