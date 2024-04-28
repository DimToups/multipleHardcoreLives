package org.mhl.multiplehardcorelives.model;

import org.bukkit.Bukkit;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.gameLogic.Player;
import org.mhl.multiplehardcorelives.model.gameLogic.Server;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.logging.Level;

public class DatabaseHandler {
    private final String url;
    private static Connection connection = null;
    private final MhlController controller;
    public DatabaseHandler(MhlController controller, String absolutePath){
        this.controller = controller;
        url = "jdbc:sqlite:" + absolutePath + ".db";
    }
    private void openConnection(){
        Bukkit.getLogger().log(Level.INFO, "Trying to connect to the database");

        //
        if(connection != null) {
            Bukkit.getLogger().log(Level.INFO, "Database was already opened. Soft-closing of the database before trying to connect to the database.");
            this.closeConnection();
        }

        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to connect to database\n" + e);
        }
    }
    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Couldn't close the connection\n" + e);
        }
    }
    public void writeChanges(Server currentServer){
        Bukkit.getLogger().log(Level.INFO, "Writing changes in the database...");
        try{
            this.openConnection();
            Statement statement = connection.createStatement();
            statement.execute("INSERT OR REPLACE INTO server (address, defaultNbLives) VALUES (\"" + Bukkit.getServer().getName() + "\", " + currentServer.getDefaultNbLives() + ");");
            Bukkit.getLogger().log(Level.INFO, "Updated the database on the server's data");
            for(Player player : currentServer.getPlayers()) {
                statement.execute("INSERT OR REPLACE INTO player (UUID, name) VALUES (\"" + player.getUuid() + "\", \"" + player.getName() + "\");");
                statement.execute("INSERT OR REPLACE INTO playerOnServerData (player, server, lives) VALUES (\"" + player.getUuid() + "\", \"" + Bukkit.getServer().getName() + "\", " + player.getLives() + ");");
            }
            Bukkit.getLogger().log(Level.INFO, "Updated the database on the players data");
            closeConnection();
        } catch (Exception e){
            Bukkit.getLogger().log(Level.SEVERE, "Failed to write changes in the database\n" + e);
        }
    }
    public void createDatabase(){
        //
        Bukkit.getLogger().log(Level.INFO, "Creating the database...");
        Bukkit.getLogger().log(Level.INFO, "Loading the database's txt schema files...");

        //
        StringBuilder serverSchema = new StringBuilder();
        StringBuilder playerOnServerDataSchema = new StringBuilder();
        StringBuilder playerSchema = new StringBuilder();

        String readString;
        InputStream iptStrmServerSchema = getClass().getResourceAsStream("/database/server-schema.txt");
        if (iptStrmServerSchema == null)
            Bukkit.getLogger().log(Level.WARNING, "iptStrmServerSchema is null");
        InputStream iptStrmPlayerOnServerDataSchema = getClass().getResourceAsStream("/database/playerOnServerData-schema.txt");
        if (iptStrmPlayerOnServerDataSchema == null)
            Bukkit.getLogger().log(Level.WARNING, "iptStrmPlayerOnServerDataSchema is null");
        InputStream iptStrmPlayerSchema = getClass().getResourceAsStream("/database/player-schema.txt");
        if (iptStrmPlayerSchema == null)
            Bukkit.getLogger().log(Level.WARNING, "iptStrmPlayerSchema is null");

        try{
            BufferedReader serverSchemaReader = new BufferedReader(new InputStreamReader(iptStrmServerSchema));
            BufferedReader playerOnServerDataSchemaReader = new BufferedReader(new InputStreamReader(iptStrmPlayerOnServerDataSchema));
            BufferedReader playerSchemaReader = new BufferedReader(new InputStreamReader(iptStrmPlayerSchema));
            while((readString = serverSchemaReader.readLine()) != null)
                serverSchema.append(readString);
            serverSchemaReader.close();
            Bukkit.getLogger().log(Level.INFO, "Loaded server schema files for the database");

            while((readString = playerOnServerDataSchemaReader.readLine()) != null)
                playerOnServerDataSchema.append(readString);
            playerOnServerDataSchemaReader.close();
            Bukkit.getLogger().log(Level.INFO, "Loaded playerOnServerData schema files for the database");

            while((readString = playerSchemaReader.readLine()) != null)
                playerSchema.append(readString);
            playerSchemaReader.close();
            Bukkit.getLogger().log(Level.INFO, "Loaded player schema files for the database");
        } catch (Exception e){
            Bukkit.getLogger().log(Level.SEVERE, "Could not read schema files for the creation of the database\n" + e);
        }

        try {
            //
            openConnection();
            Statement statement = connection.createStatement();

            //
            statement.execute(serverSchema.toString());
            Bukkit.getLogger().log(Level.INFO, "Created the server's table in the database");
            statement.execute(playerSchema.toString());
            Bukkit.getLogger().log(Level.INFO, "Created the player's table in the database");
            statement.execute(playerOnServerDataSchema.toString());
            Bukkit.getLogger().log(Level.INFO, "Created the playerOnServerData's table in the database");

            //
            closeConnection();
            Bukkit.getLogger().log(Level.INFO, "The database has been created !");
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to create the database\n" + e);
        }
    }
    @Nullable
    public Server findServer(String serverAddress){
        Bukkit.getLogger().log(Level.INFO, "Trying to find the server " + serverAddress + " in the database");
        //
        openConnection();

        //
        int defaultNbLives;
        try {
            Statement st = connection.createStatement();
            ResultSet rs1 = st.executeQuery("SELECT COUNT(address) FROM server WHERE address=\"" + serverAddress + "\"");
            if(rs1.getInt("COUNT(address)") == 0){
                this.closeConnection();
                return new Server(serverAddress);
            }

            PreparedStatement ps = connection.prepareStatement("SELECT defaultNbLives FROM server WHERE address=\"" + serverAddress + "\"");
            ResultSet rs = ps.executeQuery();
            defaultNbLives = rs.getInt("defaultNbLives");
            Bukkit.getLogger().log(Level.INFO, "Server has been found in the database");
        } catch (SQLException e) {
            Bukkit.getLogger().warning("An error occurred during the research.\n" + e);
            return new Server(serverAddress);
        }
        this.closeConnection();
        return new Server(serverAddress, defaultNbLives);
    }

    @Nullable
    public Player findPlayer(org.bukkit.entity.Player player) {
        this.openConnection();

        int nbLives = 5;
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT defaultNbLives FROM server WHERE address = \"" + Bukkit.getServer().getName() + "\";");
            ResultSet rs = ps.executeQuery();
            nbLives = rs.getInt("defaultNbLives");
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not load server's default number of lives");
        }
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT lives FROM playerOnServerData WHERE Server=\"" + Bukkit.getServer().getName() + "\" and Player=\"" + player.getUniqueId() + "\";");
            ResultSet rs = ps.executeQuery();
            nbLives = rs.getInt("lives");
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Player has not been found on this server.\n" + e);
        }

        closeConnection();
        return new Player(player.getUniqueId(), player.getName(), nbLives);
    }
}
