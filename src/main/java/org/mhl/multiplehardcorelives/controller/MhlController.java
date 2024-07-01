package org.mhl.multiplehardcorelives.controller;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.mhl.multiplehardcorelives.MultipleHardcoreLives;
import org.mhl.multiplehardcorelives.model.PlayerListener;
import org.mhl.multiplehardcorelives.model.database.DatabaseHandler;
import org.mhl.multiplehardcorelives.model.gameLogic.Player;
import org.mhl.multiplehardcorelives.model.gameLogic.Server;
import org.mhl.multiplehardcorelives.model.session.Session;
import org.mhl.multiplehardcorelives.model.session.SessionManager;
import org.mhl.multiplehardcorelives.view.PlayerList;

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
     * The plugin's custom scoreboard
     */
    private final PlayerList playerList;

    /**
     * The event listener about players data.
     */
    private final PlayerListener playerListener;

    /**
     * Creates a MhlController that will initialise its databaseHandler, server, and sessionManager.
     * @param plugin The MultipleHardcoreLives current running plugin instance.
     */
    public MhlController(MultipleHardcoreLives plugin){
        Bukkit.getLogger().log(Level.INFO, "Initialisation of the plugin...");
        //
        this.plugin = plugin;

        //
        playerListener = new PlayerListener(this);
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(playerListener, plugin);

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

        //
        try{
            World w = Bukkit.getWorld("world");
            if(w.getWorldBorder().getSize() > 0 && server.getWorldBorderLength() == 0)
                server.setWorldBorderLength((int)w.getWorldBorder().getSize());
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not set the world border length correctly");
        }
        this.reloadWorldBorder();

        //
        this.sessionManager = new SessionManager(databaseHandler.getNbOfPreviousSessions());

        this.playerList = new PlayerList(this);
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
     * Handle the event of a player joining the server. it will find the player in the database then it will add it to this Server instance.
     * @param player The player who has joined the server.
     */
    public void playerJoin(org.bukkit.entity.Player player){
        Player newPlayer = findPlayerSafelyByUUID(player.getUniqueId());
        if(newPlayer == null){
            Bukkit.getLogger().log(Level.INFO, "The player " + player.getName() + " with the UUID " + player.getUniqueId() + " has may not connected yet on the server. Instantiating a new Player...");
            newPlayer = new Player(player.getUniqueId(), player.getName(), this.server.getDefaultNbLives());
        }
        addPlayer(newPlayer);
        newPlayer.setToOnline();
        Bukkit.getLogger().log(Level.INFO, "Player " + player.getName() + " with the UUID " + player.getUniqueId() + " is now registered as online.");
        this.playerList.updatePlayerList();
    }

    /**
     * Adds a new player into the server by searching the Bukkit Player instance into the database and by sending the new player to the server if it does not contain it already.
     * @param player The Bukkit Player instance to add into the server.
     */
    public void addPlayer(org.bukkit.entity.Player player){
        Player newPlayer =  findPlayerSafelyByUUID(player.getUniqueId());
        if(newPlayer != null && !server.hasPlayerWithUUID(newPlayer.getUuid()))
            server.addPlayer(newPlayer);
    }

    /**
     * Adds a new player into the server by sending the new player to the server if it does not contain it already.
     * @param newPlayer The new player instance to add into the server.
     */
    public void addPlayer(Player newPlayer) {
        if(!server.hasPlayerWithUUID(newPlayer.getUuid()))
            server.addPlayer(newPlayer);
    }

    /**
     * Sets a number of lives to a specified player.
     * @param player The targeted player.
     * @param lives  The wanted number of lives.
     */
    public void setNbLivesOfPlayer(Player player, int lives){
        if(player.isOnline() && player.getLives() == 0 && lives > 0){
            Bukkit.getLogger().log(Level.INFO, "Resurrecting " + player.getName() +"...");
            try{
                Bukkit.getPlayer(player.getUuid()).setGameMode(GameMode.SURVIVAL);
            } catch (Exception e){
                Bukkit.getLogger().log(Level.WARNING, "Could not resurrect player " + player.getName() + ". You may have to set its gameMode to survival manually.\n" + e);
            }
        }
        player.setNbLives(lives);
        Bukkit.getLogger().log(Level.INFO, "Player \"" + player.getName() + "\" has now " + lives + " lives");
        this.playerList.updatePlayerNumberOfLives(player);
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
        Player deadPlayer = findPlayerInServer(bukkitPlayer.getUniqueId());

        //
        if(deadPlayer == null){
            Bukkit.getLogger().warning("Player with the UUID " + bukkitPlayer.getUniqueId() + " is not in the MhlController's Server instance");
            return;
        }

        //
        this.setNbLivesOfPlayer(deadPlayer, deadPlayer.getLives() - 1);

        //
        if(deadPlayer.getLives() <= 0)
            definitiveKill(deadPlayer, bukkitPlayer);
    }

    /**
     * Finds a Player instance inside the constroller's server field by searching the player with its UUID.
     * @param playerUUID The player's UUID
     * @return The corresponding Player instance
     */
    @Nullable
    private Player findPlayerInServer(UUID playerUUID) {
        for(Player player : server.getPlayers())
            if(Objects.equals(player.getUuid(), playerUUID))
                return player;
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
    public Player findPlayerSafelyByName(String name) {
        for(Player player : server.getPlayers())
            if(Objects.equals(player.getName(), name))
                return player;
        return findPlayerInDatabaseByName(name);
    }

    /**
     * Finds a player by searching it with its name.
     * @param playerUUID The player's UUID.
     * @return           The corresponding player.
     */
    @Nullable
    public Player findPlayerSafelyByUUID(UUID playerUUID) {
        for(Player player : server.getPlayers())
            if(Objects.equals(player.getUuid(), playerUUID))
                return player;
        return findPlayerInDatabaseByUUID(playerUUID);
    }

    /**
     * Finds a player by asking the databaseHandler to search it with the player's name.
     * @param name The player's name.
     * @return     The wanted player.
     */
    @Nullable
    public Player findPlayerInDatabaseByName(String name) {
        return databaseHandler.findPlayerByName(name);
    }

    /**
     * Finds a player by asking the databaseHandler to search it with the player's name.
     * @param playerUUID The player's name.
     * @return           The wanted player.
     */
    @Nullable
    public Player findPlayerInDatabaseByUUID(UUID playerUUID) {
        return databaseHandler.findPlayerByUUID(playerUUID);
    }

    /**
     * Displays information about a specified player like its number of lives to the entity who called the method.
     * @param commandSender The sender of the command.
     * @param playerName    The wanted player's name.
     */
    public void displayPlayerInformations(CommandSender commandSender, String playerName) {
        Player player = findPlayerSafelyByName(playerName);

        if(player == null){
            commandSender.sendMessage("Player \"" + playerName + "\" has not been found.");
            return;
        }

        commandSender.sendMessage("Player \"" + playerName + "\" has " + player.getLives() + " lives.");
    }

    /**
     * Displays the whole player list to the entity who called the method.
     * @param commandSender The sender of the command.
     */
    public void displayPlayerList(CommandSender commandSender) {
        List<Player> loadedPlayers, unloadedPlayers;
        loadedPlayers = server.getPlayers();
        unloadedPlayers = databaseHandler.getPlayers();

        for(Player player : loadedPlayers)
            unloadedPlayers.removeIf(dbPlayer -> dbPlayer.getUuid() == player.getUuid());

        StringBuilder message = new StringBuilder("Every player of the server: ");
        for(Player player : loadedPlayers)
            message.append("\n\t").append(player.getName()).append("(").append(player.getUuid()).append("): ").append(player.getLives()).append(" lives (loaded and is ").append(player.isOnlineToString().toLowerCase()).append(")");
        for(Player player : unloadedPlayers)
            message.append("\n\t").append(player.getName()).append("(").append(player.getUuid()).append("): ").append(player.getLives()).append(" lives (unloaded)");

        commandSender.sendMessage(message.toString());
    }

    /**
     * Handle the event of a player quitting the server. It will find the player in this Server instance and set its state to offline.
     * @param player The gone player.
     */
    public void playerQuit(org.bukkit.entity.Player player) {
        Player gonePlayer = findPlayerSafelyByUUID(player.getUniqueId());
        if(gonePlayer == null) {
            Bukkit.getLogger().log(Level.WARNING, "Player " + player.getName() + " with the UUID " + player.getUniqueId() + " is null. Could not register the player as offline");
            return;
        }
        gonePlayer.setToOffline();
        Bukkit.getLogger().log(Level.INFO, "Player " + player.getName() + " with the UUID " + player.getUniqueId() + " is now registered as offline");
    }

    /**
     * Verifies if the server's information is coherent.
     */
    public void verifyServerState() {
        Bukkit.getLogger().log(Level.INFO, "Starting the verification of the server's informations...");
        List<Player> players = server.getPlayers();
        for(int i = 0; i < players.size(); i++){
            for (int j = i + 1; j < players.size(); j++){
                if(players.get(i).getUuid() == players.get(j).getUuid() && Objects.equals(players.get(i).getName(), players.get(j).getName()) && players.get(i).getLives() == players.get(j).getLives())
                    Bukkit.getLogger().log(Level.WARNING, "Two registered players have the same UUID, name, and number of lives. They both have this identity :\n\t" + players.get(i).getName() + " (" + players.get(i).getUuid() + ") : " + players.get(i).getLives() + " lives");
                if(players.get(i).getUuid() == players.get(j).getUuid())
                    Bukkit.getLogger().log(Level.WARNING, "Two registered players have the same UUID. Their name are " + players.get(i).getName() + " and " + players.get(j).getName());
                if(Objects.equals(players.get(i).getName(), players.get(j).getName()))
                    Bukkit.getLogger().log(Level.WARNING, "Two registered players have the same name. Their name are " + players.get(i).getName() + " and their UUID are " + players.get(i).getUuid() + ", and " + players.get(j).getUuid());
            }
        }
        Bukkit.getLogger().log(Level.INFO, "Finished the verification of the server's players");
        if(!Objects.equals(server.getAddress(), Bukkit.getServer().getName()))
            Bukkit.getLogger().log(Level.WARNING, "The MhlController's Server instance and the current server have not the same address. The MhlController's Server instance has for address \"" + server.getAddress() + "\" and the current server has for address + \"" + Bukkit.getServer().getName() + "\"");
        Bukkit.getLogger().log(Level.INFO, "Finished the verification of the server's address");
        if(server.getDefaultNbLives() <= 0)
            Bukkit.getLogger().log(Level.WARNING, "The server have a non positive default number of lives. It has " + server.getDefaultNbLives() + " as a default number of lives");
        Bukkit.getLogger().log(Level.INFO, "Finished the verification of the server's default number of lives");
        Bukkit.getLogger().log(Level.INFO, "Finished the verification of the server");
    }

    public void displayDefaultNumberOfLives(CommandSender sender) {
        sender.sendMessage("Default number of lives is set to " + server.getDefaultNbLives());
    }

    public void setWorldBorderLength(int length){
        Bukkit.getLogger().log(Level.INFO, "Setting the new world border length to " + length + " blocks");
        try{
            World surface = Bukkit.getWorld("world");
            WorldBorder ws = surface.getWorldBorder();
            ws.setCenter(surface.getSpawnLocation());
            ws.setSize(length);

            World nether = Bukkit.getWorld("world_nether");
            WorldBorder wn = nether.getWorldBorder();
            wn.setCenter(new Location(nether, nether.getSpawnLocation().getX() / 8, nether.getSpawnLocation().getY() / 8, nether.getSpawnLocation().getZ()));
            wn.setSize(length);

            this.server.setWorldBorderLength(length);
            Bukkit.getLogger().log(Level.INFO, "World border length has been set to " + length + " block");
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not set the world border length\n" + e);
        }
    }

    public Double getWorldBorderLength() {
        try{
            return Bukkit.getWorld("world").getWorldBorder().getSize();
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not set the world border length\n" + e);
        }
        return null;
    }

    public void reloadWorldBorder() {
        this.setWorldBorderLength(this.server.getWorldBorderLength());
    }

    public void playerDeath(org.bukkit.entity.Player player) {
        this.decrementLivesOfPlayer(player);
    }

    public List<Session> getSessions() {
        return this.sessionManager.getSessions();
    }
}
