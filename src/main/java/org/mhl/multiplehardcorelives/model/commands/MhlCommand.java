package org.mhl.multiplehardcorelives.model.commands;

import org.bukkit.command.CommandExecutor;
import org.mhl.multiplehardcorelives.controller.MhlController;

/**
 * An abstract class serving as a template to every command of the plugin.
 */
public abstract class MhlCommand implements CommandExecutor {
    protected final MhlController controller;

    /**
     * Initialise a MhlCommand class by using the plugin's controller.
     * @param controller The plugin's controller
     */
    public MhlCommand(MhlController controller){
        this.controller = controller;
    }
}
