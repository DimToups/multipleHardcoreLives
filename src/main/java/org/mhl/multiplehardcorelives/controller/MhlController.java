package org.mhl.multiplehardcorelives.controller;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.mhl.multiplehardcorelives.MultipleHardcoreLives;
import org.mhl.multiplehardcorelives.model.*;
import org.mhl.multiplehardcorelives.model.gameLogic.Player;
import org.mhl.multiplehardcorelives.model.gameLogic.Server;
import org.mhl.multiplehardcorelives.model.gameLogic.SessionManager;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.logging.Level;

public class MhlController {
    private final MultipleHardcoreLives plugin;
    private final Server server;
    private final SessionManager sessionManager;
    private final DatabaseHandler databaseHandler;
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
    public void reset(){

    }
    public void startSession(){
        sessionManager.startSession();
    }
    public void endSession(){
        if(sessionManager.isSessionActive()) {
            this.sessionManager.endSession();
            this.writeChanges();
        }
        else
            Bukkit.getLogger().warning("Session has already stopped");
    }
    public void addPlayer(org.bukkit.entity.Player player){
        Player newPlayer =  databaseHandler.findPlayer(player);
        if(!server.getPlayers().contains(newPlayer))
            server.addPlayer(newPlayer);
    }
    public void setNbLivesOfPlayer(Player player, int lives){
        player.setNbLives(lives);
        Bukkit.getLogger().log(Level.INFO, "Player \"" + player.getName() + "\" has now " + lives + " lives");
        if(lives > server.getDefaultNbLives())
            Bukkit.getLogger().log(Level.WARNING, "Player \"" + player.getName() + "\" has more lives than the default number of " + server.getDefaultNbLives());
    }
    public void decrementLivesOfPlayer(org.bukkit.entity.Player player){
        //
        if(!this.sessionManager.isSessionActive()){
            Bukkit.getLogger().warning("Tried to decrement number of lives of a player while the session has not started yet");
            return;
        }

        //
        Player deadPlayer = null;
        for(Player serverPlayer : server.getPlayers()){
            if(serverPlayer.getUuid() == player.getUniqueId()) {
                deadPlayer = serverPlayer;
                break;
            }
        }

        //
        if(deadPlayer == null){
            Bukkit.getLogger().warning("Player with the UUID " + player.getUniqueId() + " is not in the MhlController's Server instance");
            return;
        }

        //
        deadPlayer.setNbLives(deadPlayer.getLives() - 1);

        //


        //
        if(deadPlayer.getLives() <= 0)
            definitiveKill(deadPlayer);
    }
    private void definitiveKill(Player player){

    }
    public Plugin getPlugin(){
        return this.plugin;
    }

    public void serverClosing() {
        Bukkit.getLogger().log(Level.INFO, "Closing the server...");
        this.endSession();
    }

    public void setDefaultNumberOfLives(int defaultNbLives) {
        if(sessionManager.isSessionActive()){
            Bukkit.getLogger().log(Level.WARNING, "Cannot change the default number of lives of the server, the session is still running.");
            return;
        }
        this.server.setDefaultNbLives(defaultNbLives);
    }

    public void writeChanges() {
        this.databaseHandler.writeChanges(this.server);
    }

    @Nullable
    public Player findPlayer(String name) {
        Player targetedPlayer = null;
        for(Player player : server.getPlayers()){
            if(Objects.equals(player.getName(), name)) {
                targetedPlayer = player;
                break;
            }
        }
        return targetedPlayer;
    }
}
