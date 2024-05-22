package org.mhl.multiplehardcorelives;

import org.bukkit.plugin.java.JavaPlugin;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.commands.*;

/**
 * The main class of the plugin.
 */
public final class MultipleHardcoreLives extends JavaPlugin {
    /**
     * The plugin's controller. It will manage almost every aspect of the plugin once it's initialised.
     */
    private MhlController controller;

    /**
     *  Initialises the plugin by initialising its controller and loading every custom command.
     */
    @Override
    public void onEnable() {
        //
        controller = new MhlController(this);

        //
        this.loadCommands();
    }

    /**
     *  Loads every custom command from the plugin.
     *  Each command need to be added manually in this method for it to be loaded.
     */
    private void loadCommands(){
        this.getCommand("startSession").setExecutor(new CommandStartSession(controller));
        this.getCommand("endSession").setExecutor(new CommandEndSession(controller));
        this.getCommand("setDefaultNumberOfLives").setExecutor(new CommandSetDefaultNumberOfLives(controller));
        this.getCommand("writeChanges").setExecutor(new CommandWriteChanges(controller));
        this.getCommand("setNumberOfLivesToPlayer").setExecutor(new CommandSetNumberOfLivesToPlayer(controller));
        this.getCommand("resetServer").setExecutor(new CommandResetServer(controller));
        this.getCommand("playerInformations").setExecutor(new CommandPlayerInformations(controller));
        this.getCommand("playerList").setExecutor(new CommandPlayerList(controller));
        this.getCommand("verifyServerState").setExecutor(new CommandVerifyServerState(controller));
    }

    /**
     *  Disable the plugin. It asks the controller to end properly the plugin.
     */
    @Override
    public void onDisable() {
        controller.serverClosing();
    }
}
