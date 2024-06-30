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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A class that interacts with the plugin's database. It tries to handle every aspect of the database.
 */
public class DatabaseHandler {
    /**
     * The url of the database.
     */
    private final String url;
    /**
     * The Connection's instance to the database
     */
    private Connection connection = null;
    /**
     * The plugin's controller.
     */
    private final MhlController controller;

    /**
     * Initialises the DatabaseHandler. It sets the path to the database by using the absolute path of the plugin, and defines the controller of the class.
     * @param controller The controller of the class.
     * @param absolutePath The absolute path to the plugin.
     */
    public DatabaseHandler(MhlController controller, String absolutePath){
        this.controller = controller;
        url = "jdbc:sqlite:" + absolutePath + ".db";
    }

    /**
     * Opens the connection to the database using the Connection's instance of the class.
     */
    private void openConnection(){
        Bukkit.getLogger().log(Level.INFO, "Trying to connect to the database");

        //
        if(connection != null) {
            Bukkit.getLogger().log(Level.INFO, "Database was already opened. Closing of the database before trying to connect to the database.");
            this.closeConnection();
        }

        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to connect to database\n" + e);
        }
    }

    /**
     * Closes the connection to the database using the Connection's instance of the class.
     */
    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Couldn't close the connection\n" + e);
        }
    }

    /**
     * Writes every change from the server into the database.
     * @param currentServer The running server used by the controller.
     */
    public void writeChanges(Server currentServer){
        Bukkit.getLogger().log(Level.INFO, "Writing changes in the database...");
        try{
            this.openConnection();
            Statement statement = connection.createStatement();
            statement.execute("INSERT OR REPLACE INTO server (address, defaultNbLives, worldBorderLength) VALUES (\"" + Bukkit.getServer().getName() + "\", " + currentServer.getDefaultNbLives() + ", " + currentServer.getWorldBorderLength() + ");");
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

    /**
     * Creates the used database using its initialised String url instance.
     */
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

    /**
     * Finds the wanted server in the database and converts it into a new Server instance.
     * @param serverAddress The address of the server
     * @return A new Server instance corresponding to its address.
     */
    @Nullable
    public Server findServer(String serverAddress){
        Bukkit.getLogger().log(Level.INFO, "Trying to find the server " + serverAddress + " in the database");
        //
        openConnection();

        //
        int defaultNbLives;
        int worldBorderLength;
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

            ps = connection.prepareStatement("SELECT worldBorderLength FROM server WHERE address=\"" + serverAddress + "\"");
            rs = ps.executeQuery();
            worldBorderLength = rs.getInt("worldBorderLength");

            Bukkit.getLogger().log(Level.INFO, "Server has been found in the database");
        } catch (SQLException e) {
            Bukkit.getLogger().warning("An error occurred during the research.\n" + e);
            return new Server(serverAddress);
        }
        this.closeConnection();
        return new Server(serverAddress, defaultNbLives, worldBorderLength);
    }

    /**
     * Finds the wanted player in the database and converts it into a new Player instance.
     * @param player The targeted Bukkit player class.
     * @return A new Player instance corresponding to the Bukkit player class.
     */
    @Nullable
    public Player findPlayerByName(org.bukkit.entity.Player player) {
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

    /**
     * Finds the wanted player in the database and converts it into a new Player instance.
     * @param name The player's name.
     * @return A new Player instance corresponding to the player's name.
     */
    @Nullable
    public Player findPlayerByName(String name) {
        Player player = null;

        //
        openConnection();
        try{
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM playerOnServerData WHERE player IN(SELECT UUID FROM player WHERE name =\""+ name + "\");");
            player = new Player(UUID.fromString(rs.getString("player")), name, rs.getInt("lives"));
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not find the player " + name + " in the database");
        }

        return player;
    }

    /**
     * Finds the wanted player in the database and converts it into a new Player instance.
     * @param playerUUID The player's name.
     * @return           A new Player instance corresponding to the player's name.
     */
    @Nullable
    public Player findPlayerByUUID(UUID playerUUID) {
        Player player = null;

        //
        openConnection();
        try{
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT ps.player AS player, p.name AS name, ps.lives AS lives FROM playerOnServerData ps JOIN player p ON ps.player = p.UUID WHERE p.UUID=\"" + playerUUID + "\";");
            player = new Player(UUID.fromString(rs.getString("player")), rs.getString("name"), rs.getInt("lives"));
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not find the player with the UUID " + playerUUID.toString() + " in the database\n" + e);
        }

        return player;
    }

    /**
     * Sets the number of lives of a player from a server into the database.
     * @param player The targeted player.
     * @param number The wanted number of lives.
     */
    public void setNumberOfLivesToPlayer(Player player, int number) {

    }

    /**
     * Sets the number of lives to every player from a server into the database
     * @param nbLives The wanted number of lives.
     */
    public void setNumberOfLivesToEveryPlayer(int nbLives) {
        Bukkit.getLogger().log(Level.INFO, "Started the setting the number of lives of every player");
        this.openConnection();
        List<Player> players = new ArrayList<>();
        try {
            Bukkit.getLogger().log(Level.INFO, "Loading every player");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT ps.player, p.name FROM playerOnServerData ps JOIN player p ON p.UUID = ps.player WHERE server=\"" + Bukkit.getServer().getName() + "\";");

            //
            while(rs.next())
                players.add(new Player(UUID.fromString(rs.getString("player")), rs.getString("name"), nbLives));
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load every players\n" + e);
            Bukkit.getLogger().log(Level.SEVERE, "Stopping the setting of the number of lives of every player");
            return;
        }

        try {
            Bukkit.getLogger().log(Level.INFO, "Setting the number of lives of every player");
            Statement st = connection.createStatement();
            for(Player player : players) {
                st.execute("UPDATE playerOnServerData SET lives=" + nbLives + " WHERE player=\"" + player.getUuid() + "\" AND server=\"" + Bukkit.getServer().getName() + "\";");
                Bukkit.getLogger().log(Level.INFO, "Player \"" + player.getName() + "\" has now " + nbLives + " lives");
            }
        } catch (Exception e){
            Bukkit.getLogger().log(Level.SEVERE, "Could not write changes in the database\n" + e);
            Bukkit.getLogger().log(Level.SEVERE, "Stopping the setting of the number of lives of every player");
            return;
        }
        Bukkit.getLogger().log(Level.INFO, "Every player has now " + nbLives + " lives");
        closeConnection();
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();

        try{
            openConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT p.UUID, p.name, ps.lives FROM player p JOIN playerOnServerData ps ON ps.player = p.UUID WHERE ps.server=\"" + Bukkit.getServer().getName() + "\";");
            while (rs.next())
                players.add(new Player(UUID.fromString(rs.getString("UUID")), rs.getString("name"), rs.getInt("lives")));
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Couldn't find every player in the database.\n" + e);
        }

        return players;
    }
}
