package org.mhl.multiplehardcorelives.controller;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.mhl.multiplehardcorelives.MultipleHardcoreLives;
import org.mhl.multiplehardcorelives.model.*;
import org.mhl.multiplehardcorelives.model.gameLogic.Player;
import org.mhl.multiplehardcorelives.model.gameLogic.Server;
import org.mhl.multiplehardcorelives.model.gameLogic.SessionManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

/**
 * The plugin's controller. It handles almost every aspect of the plugin's life.
 */
public class MhlController {
    /**
     * The MultipleHardcoreLives current running plugin instance.
     */
    private final MultipleHardcoreLives plugin;

    /**
     * The current running server model.
     */
    private final Server server;

    /**
     * The plugin's session manager. It will handle events from its session.
     */
    private final SessionManager sessionManager;

    /**
     * A model made to interact with the plugin's database.
     */
    private final DatabaseHandler databaseHandler;

    /**
     * Creates a MhlController that will initialise its databaseHandler, server, and sessionManager.
     * @param plugin The MultipleHardcoreLives current running plugin instance.
     */
    public MhlController(MultipleHardcoreLives plugin){
        Bukkit.getLogger().log(Level.INFO, "Initialisation of the plugin...");
        //
        this.plugin = plugin;

        //
        databaseHandler = new DatabaseHandler(this, plugin.getDataFolder().getAbsolutePath());
        if(!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();
        databaseHandler.createDatabase();
        Server foundServer = databaseHandler.findServer(Bukkit.getServer().getName());
        if(foundServer == null)
            server = new Server(Bukkit.getServer().getName());
        else
            server = foundServer;

        this.sessionManager = new SessionManager(this);
    }

    /**
     * Resets the server by ending the session if its running, and by setting the default number of lives back to 5.
     */
    public void resetServer(){
        Bukkit.getLogger().log(Level.WARNING, "Resetting the server's data.");
        if(this.sessionManager.isSessionActive())
            this.sessionManager.endSession();
        this.setDefaultNumberOfLives(5);
    }

    /**
     * Starts the session by telling it to the sessionManager.
     */
    public void startSession(){
        sessionManager.startSession();
    }

    /**
     * Ends the session by telling it to the sessionManager and by asking to write changes into the database.
     */
    public void endSession(){
        if(sessionManager.isSessionActive()) {
            this.sessionManager.endSession();
            this.writeChanges();
        }
        else
            Bukkit.getLogger().warning("Session has already stopped");
    }

    /**
     * Adds a new player into the server by searching the Bukkit Player instance into the database and by sending the new player to the server if it does not contain it already.
     * @param player The Bukkit Player instance to add into the server.
     */
    public void addPlayer(org.bukkit.entity.Player player){
        Player newPlayer =  databaseHandler.findPlayer(player);
        if(!server.getPlayers().contains(newPlayer))
            server.addPlayer(newPlayer);
    }

    /**
     * Adds a new player into the server by sending the new player to the server if it does not contain it already.
     * @param newPlayer The new player instance to add into the server.
     */
    public void addPlayer(Player newPlayer) {
        if(!server.getPlayers().contains(newPlayer))
            server.addPlayer(newPlayer);
    }

    /**
     * Sets a number of lives to a specified player.
     * @param player The targeted player.
     * @param lives  The wanted number of lives.
     */
    public void setNbLivesOfPlayer(Player player, int lives){
        player.setNbLives(lives);
        Bukkit.getLogger().log(Level.INFO, "Player \"" + player.getName() + "\" has now " + lives + " lives");
        if(lives > server.getDefaultNbLives())
            Bukkit.getLogger().log(Level.WARNING, "Player \"" + player.getName() + "\" has more lives than the default number of " + server.getDefaultNbLives());
    }

    /**
     * Reduces the number of lives by one to a specified player.
     * @param bukkitPlayer The Bukkit Player instance which lost a life.
     */
    public void decrementLivesOfPlayer(org.bukkit.entity.Player bukkitPlayer){
        //
        if(!this.sessionManager.isSessionActive()){
            Bukkit.getLogger().warning("Tried to decrement number of lives of a player while the session has not started yet");
            return;
        }

        //
        Player deadPlayer = findPlayer(bukkitPlayer.getUniqueId());

        //
        if(deadPlayer == null){
            Bukkit.getLogger().warning("Player with the UUID " + bukkitPlayer.getUniqueId() + " is not in the MhlController's Server instance");
            return;
        }

        //
        deadPlayer.setNbLives(deadPlayer.getLives() - 1);

        //
        if(deadPlayer.getLives() <= 0)
            definitiveKill(deadPlayer, bukkitPlayer);
    }

    @Nullable
    private Player findPlayer(UUID uniqueId) {
        for(Player p : server.getPlayers())
            if(p.getUuid() == uniqueId)
                return p;
        return null;
    }

    /**
     * Kills permanently a player by setting its game mode to spectator
     * @param player       The dead player.
     * @param bukkitPlayer The dead Bukkit player.
     */
    private void definitiveKill(Player player, org.bukkit.entity.Player bukkitPlayer){
        bukkitPlayer.setGameMode(GameMode.SPECTATOR);
        bukkitPlayer.sendTitle("You are out of lives !", "", 0, 70, 40);
        Bukkit.getLogger().log(Level.INFO, player.getName() + " has definitively died");
    }

    /**
     * Sends the plugin.
     * @return The plugin.
     */
    public Plugin getPlugin(){
        return this.plugin;
    }

    /**
     * Closes the server by ending the current session.
     */
    public void serverClosing() {
        Bukkit.getLogger().log(Level.INFO, "Closing the server...");
        this.endSession();
    }

    /**
     * Sets the default number of lives to every player who has ever joined the server only if the session is not running.
     * @param defaultNbLives The wanted default number of lives.
     */
    public void setDefaultNumberOfLives(int defaultNbLives) {
        if(sessionManager.isSessionActive()){
            Bukkit.getLogger().log(Level.WARNING, "Cannot change the default number of lives of the server, the session is still running.");
            return;
        }
        this.databaseHandler.setNumberOfLivesToEveryPlayer(defaultNbLives);
        this.server.setDefaultNbLives(defaultNbLives);
    }

    /**
     * Write every change into the database by asking the databaseHandler to do it.
     */
    public void writeChanges() {
        this.databaseHandler.writeChanges(this.server);
    }

    /**
     * Finds a player by searching it with its name.
     * @param name The player's name.
     * @return     The corresponding player.
     */
    @Nullable
    public Player findPlayer(String name) {
        for(Player player : server.getPlayers())
            if(Objects.equals(player.getName(), name))
                return player;
        return findPlayerInDatabase(name);
    }

    /**
     * Finds a player by asking the databaseHandler to search it with the player's name.
     * @param name The player's name.
     * @return     The wanted player.
     */
    @Nullable
    public Player findPlayerInDatabase(String name) {
        return databaseHandler.findPlayer(name);
    }

    public void displayPlayerInformations(CommandSender commandSender, String playerName) {
        Player player = findPlayer(playerName);

        if(player == null){
            commandSender.sendMessage("Player \"" + playerName + "\" has not been found.");
            return;
        }

        commandSender.sendMessage("Player \"" + playerName + "\" has " + player.getLives() + " lives.");
    }

    public void displayPlayerList(CommandSender commandSender) {
        List<Player> loadedPlayers, unloadedPlayers;
        loadedPlayers = server.getPlayers();
        unloadedPlayers = databaseHandler.getPlayers();

        for(Player player : loadedPlayers)
            unloadedPlayers.removeIf(dbPlayer -> dbPlayer.getUuid() == player.getUuid());

        StringBuilder message = new StringBuilder("Every player of the server: ");
        for(Player player : loadedPlayers)
            message.append("\n\t" + player.getName() + ": " + player.getLives() + " lives (loaded)");
        for(Player player : unloadedPlayers)
            message.append("\n\t" + player.getName() + ": " + player.getLives() + " lives (unloaded)");

        commandSender.sendMessage(message.toString());
    }
}
