package org.mhl.multiplehardcorelives.model.database;

import org.bukkit.Bukkit;
import org.mhl.multiplehardcorelives.controller.MhlController;
import org.mhl.multiplehardcorelives.model.gameLogic.Player;
import org.mhl.multiplehardcorelives.model.gameLogic.Server;
import org.mhl.multiplehardcorelives.model.session.Session;
import org.mhl.multiplehardcorelives.model.session.SessionEvent;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
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
    protected Connection connection = null;
    /**
     * The plugin's controller.
     */
    private final MhlController controller;

    /**
     * The class's own tableFactory
     */
    private final TableFactory tableFactory;

    /**
     * Initialises the DatabaseHandler. It sets the path to the database by using the absolute path of the plugin, and defines the controller of the class.
     * @param controller The controller of the class.
     * @param absolutePath The absolute path to the plugin.
     */
    public DatabaseHandler(MhlController controller, String absolutePath){
        this.controller = controller;
        this.tableFactory = new TableFactory(this);
        url = "jdbc:sqlite:" + absolutePath + ".db";
    }

    /**
     * Opens the connection to the database using the Connection's instance of the class.
     */
    protected void openConnection(){
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
    protected void closeConnection() {
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
            for(Session session : controller.getSessions()){
                Date sessionStart = session.getSessionStart().getTime();
                Date sessionEnd = session.getSessionEnd().getTime();
                String request = "INSERT OR REPLACE INTO session (server, sessionNumber, sessionStart, sessionEnd) VALUES (\""
                        + Bukkit.getServer().getName() + "\", "
                        + session.getSessionNumber()
                        + ", \"" + dateToSQLDate(sessionStart) + "\""
                        + ", \"" + dateToSQLDate(sessionEnd) + "\");";
                statement.execute(request);
                for(SessionEvent event : session.getEvents())
                    statement.execute("INSERT OR REPLACE INTO sessionEvent (server, sessionNumber, eventId, eventDate, description, type) VALUES (\""
                            + Bukkit.getServer().getName() + "\", "
                            + session.getSessionNumber() + ", "
                            + event.eventId + ", \""
                            + dateToSQLDate(event.date.getTime()) + "\", \""
                            + event.description+ "\", \""
                            + event.event + "\");");
            }
            closeConnection();
        } catch (Exception e){
            Bukkit.getLogger().log(Level.SEVERE, "Failed to write changes in the database\n" + e);
        }
    }

    private String dateToSQLDate(Date date){
        return (1900 + date.getYear()) + "/" + (date.getMonth() + 1) + "/" + date.getDay() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
    }

    /**
     * Creates the used database using its initialised String url instance.
     */
    public void createDatabase(){
        //
        Bukkit.getLogger().log(Level.INFO, "Creating the database...");

        tableFactory.createPlayerTable();
        tableFactory.createServerTable();
        tableFactory.createPlayerOnServerDataTable();
        tableFactory.createEventTypeTable();
        tableFactory.createSessionTable();
        tableFactory.createSessionEventTable();
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

    /**
     * Searches every player from the server in the database
     * @return Every player from the server
     */
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

    /**
     * Searches the number of previous sessions from the current server in the database
     * @return The number of previous sessions
     */
    public int getNbOfPreviousSessions(){
        int nbOfPreviousSessions = 0;
        Bukkit.getLogger().log(Level.WARNING, "Trying to find the number of previous sessions");

        try{
            openConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(server) FROM session WHERE server=\"" + Bukkit.getServer().getName() +"\";");
            nbOfPreviousSessions = rs.getInt("COUNT(server)");
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not find the number of previous sessions");
        }

        return nbOfPreviousSessions;
    }
}
