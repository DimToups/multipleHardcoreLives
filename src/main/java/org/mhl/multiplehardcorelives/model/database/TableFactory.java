package org.mhl.multiplehardcorelives.model.database;

import org.bukkit.Bukkit;
import org.mhl.multiplehardcorelives.model.session.enums.SessionEvents;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * A class for creating SQLite tables in the database
 */
public class TableFactory {
    /**
     * The instance's databaseHandler which it will rely on for its requests
     */
    private final DatabaseHandler dbHandler;

    /**
     * Creates a TableFactory instance by using a DatabaseHandler
     * @param dbHandler The instance's future databaseHandler which it will rely on for its requests
     */
    public TableFactory(DatabaseHandler dbHandler){
        this.dbHandler = dbHandler;
    }

    /**
     * Creates a player table inside the database
     */
    public void createPlayerTable(){
        StringBuilder serverSchema = new StringBuilder();
        String readString;

        Bukkit.getLogger().log(Level.INFO, "Loading the database's txt schema file...");
        InputStream iptStrmPlayerSchema = getClass().getResourceAsStream("/database/player-schema.txt");
        if (iptStrmPlayerSchema == null) {
            Bukkit.getLogger().log(Level.WARNING, "iptStrmPlayerSchema is null");
            return;
        }
        try{
            BufferedReader playerSchemaReader = new BufferedReader(new InputStreamReader(iptStrmPlayerSchema));

            while((readString = playerSchemaReader.readLine()) != null)
                serverSchema.append(readString);
            playerSchemaReader.close();
            Bukkit.getLogger().log(Level.INFO, "Loaded server schema files for the database");

            //
            dbHandler.openConnection();
            Statement statement = dbHandler.connection.createStatement();
            statement.execute(serverSchema.toString());
            Bukkit.getLogger().log(Level.INFO, "Created the server's table in the database");
            dbHandler.closeConnection();
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not create the server's table in the database");
        }
    }

    /**
     * Creates a server table inside the database
     */
    public void createServerTable(){
        StringBuilder serverSchema = new StringBuilder();
        String readString;

        Bukkit.getLogger().log(Level.INFO, "Loading the database's txt schema file...");
        InputStream iptStrmServerSchema = getClass().getResourceAsStream("/database/server-schema.txt");
        if (iptStrmServerSchema == null) {
            Bukkit.getLogger().log(Level.WARNING, "iptStrmServerSchema is null");
            return;
        }
        try{
            BufferedReader serverSchemaReader = new BufferedReader(new InputStreamReader(iptStrmServerSchema));

            while((readString = serverSchemaReader.readLine()) != null)
                serverSchema.append(readString);
            serverSchemaReader.close();
            Bukkit.getLogger().log(Level.INFO, "Loaded player schema files for the database");

            //
            dbHandler.openConnection();
            Statement statement = dbHandler.connection.createStatement();
            statement.execute(serverSchema.toString());
            Bukkit.getLogger().log(Level.INFO, "Created the player's table in the database");
            dbHandler.closeConnection();
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not create the player's table in the database");
        }
    }

    /**
     * Creates a playerOnServerData table inside the database
     */
    public void createPlayerOnServerDataTable(){
        StringBuilder playerOnServerDataSchema = new StringBuilder();
        String readString;

        Bukkit.getLogger().log(Level.INFO, "Loading the database's txt schema file...");
        InputStream iptStrmPlayerOnServerDataSchema = getClass().getResourceAsStream("/database/playerOnServerData-schema.txt");
        if (iptStrmPlayerOnServerDataSchema == null) {
            Bukkit.getLogger().log(Level.WARNING, "iptStrmPlayerOnServerDataSchema is null");
            return;
        }
        try{
            BufferedReader playerOnServerDataSchemaReader = new BufferedReader(new InputStreamReader(iptStrmPlayerOnServerDataSchema));

            while((readString = playerOnServerDataSchemaReader.readLine()) != null)
                playerOnServerDataSchema.append(readString);
            playerOnServerDataSchemaReader.close();
            Bukkit.getLogger().log(Level.INFO, "Loaded server schema files for the database");

            //
            dbHandler.openConnection();
            Statement statement = dbHandler.connection.createStatement();
            statement.execute(playerOnServerDataSchema.toString());
            Bukkit.getLogger().log(Level.INFO, "Created the playerOnServerData's table in the database");
            dbHandler.closeConnection();
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not create the playerOnServerData's table in the database");
        }
    }

    /**
     * Creates an eventType table inside the database
     */
    public void createEventTypeTable(){
        StringBuilder eventTypeSchema = new StringBuilder();
        String readString;

        Bukkit.getLogger().log(Level.INFO, "Loading the database's txt schema file...");
        InputStream iptStrmEventTypeSchema = getClass().getResourceAsStream("/database/eventType-schema.txt");
        if (iptStrmEventTypeSchema == null) {
            Bukkit.getLogger().log(Level.WARNING, "iptStrmEventTypeSchema is null");
            return;
        }
        try{
            BufferedReader EventTypeSchemaReader = new BufferedReader(new InputStreamReader(iptStrmEventTypeSchema));

            while((readString = EventTypeSchemaReader.readLine()) != null)
                eventTypeSchema.append(readString);
            EventTypeSchemaReader.close();
            Bukkit.getLogger().log(Level.INFO, "Loaded eventType schema files for the database");

            //
            dbHandler.openConnection();
            Statement statement = dbHandler.connection.createStatement();
            statement.execute(eventTypeSchema.toString());
            Bukkit.getLogger().log(Level.INFO, "Created the eventType's table in the database");

            //
            for(SessionEvents event : SessionEvents.values())
                statement.execute("INSERT OR REPLACE INTO eventType (type) VALUES (\"" + SessionEvents.cleanName(event) + "\")");

            //
            dbHandler.closeConnection();
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not create the eventType's table in the database");
        }
    }

    /**
     * Creates a session table inside the database
     */
    public void createSessionTable(){
        StringBuilder sessionSchema = new StringBuilder();
        String readString;

        Bukkit.getLogger().log(Level.INFO, "Loading the database's txt schema file...");
        InputStream iptStrmSesssionSchema = getClass().getResourceAsStream("/database/session-schema.txt");
        if (iptStrmSesssionSchema == null) {
            Bukkit.getLogger().log(Level.WARNING, "iptStrmSesssionSchema is null");
            return;
        }
        try{
            BufferedReader sessionSchemaReader = new BufferedReader(new InputStreamReader(iptStrmSesssionSchema));

            while((readString = sessionSchemaReader.readLine()) != null)
                sessionSchema.append(readString);
            sessionSchemaReader.close();
            Bukkit.getLogger().log(Level.INFO, "Loaded session schema files for the database");

            //
            dbHandler.openConnection();
            Statement statement = dbHandler.connection.createStatement();
            statement.execute(sessionSchema.toString());
            Bukkit.getLogger().log(Level.INFO, "Created the session's table in the database");
            dbHandler.closeConnection();
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not create the session's table in the database");
        }
    }

    /**
     * Creates a sessionEvent table inside the database
     */
    public void createSessionEventTable(){
        StringBuilder sessionEventSchema = new StringBuilder();
        String readString;

        Bukkit.getLogger().log(Level.INFO, "Loading the database's txt schema file...");
        InputStream iptStrmSessionEventSchema = getClass().getResourceAsStream("/database/sessionEvent-schema.txt");
        if (iptStrmSessionEventSchema == null) {
            Bukkit.getLogger().log(Level.WARNING, "iptStrmSessionEventSchema is null");
            return;
        }
        try{
            BufferedReader sessionEventSchemaReader = new BufferedReader(new InputStreamReader(iptStrmSessionEventSchema));

            while((readString = sessionEventSchemaReader.readLine()) != null)
                sessionEventSchema.append(readString);
            sessionEventSchemaReader.close();
            Bukkit.getLogger().log(Level.INFO, "Loaded sessionEvent schema files for the database");

            //
            dbHandler.openConnection();
            Statement statement = dbHandler.connection.createStatement();
            statement.execute(sessionEventSchema.toString());
            Bukkit.getLogger().log(Level.INFO, "Created the sessionEvent's table in the database");
            dbHandler.closeConnection();
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING, "Could not create the sessionEvent's table in the database");
        }
    }
}
