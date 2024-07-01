package org.mhl.multiplehardcorelives.model.session;

import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.mhl.multiplehardcorelives.model.enums.SessionEvents;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;

/**
 * A class that contains the logic of the plugin for its sessions.
 */
public class SessionManager {

    /**
     * A boolean indicating if the session is active or not.
     */
    private boolean isSessionActive = false;

    /**
     * The list of sessions the server had host since its launch
     */
    private final ArrayList<Session> sessions = new ArrayList<>();

    /**
     * The number of previous sessions
     */
    private final int nbOfPreviousSessions;

    /**
     * The currently running session
     */
    private Session currentSession;

    /**
     * Initialises a SessionManager that will listen to players events.
     * @param nbOfPreviousSessions The number of previous sessions
     */
    public SessionManager(int nbOfPreviousSessions){
        this.nbOfPreviousSessions = nbOfPreviousSessions;
        Bukkit.getLogger().log(Level.INFO, "Now listening to player informations");
    }

    /**
     * Sends the state of the session.
     * @return The state of the session.
     */
    public boolean isSessionActive() {
        return isSessionActive;
    }

    /**
     * Starts the session by setting the field isSessionActive to true.
     */
    public void startSession(){
        //
        if(isSessionActive){
            Bukkit.getLogger().warning("Session has already started");
            return;
        }

        //
        this.sessions.add(new Session(nbOfPreviousSessions + sessions.size() + 1, Calendar.getInstance()));
        this.currentSession = sessions.getLast();

        //
        isSessionActive = true;
    }

    /**
     * Ends the sessions by setting the field isSessionActive to false.
     */
    public void endSession(){
        if(!isSessionActive){
            Bukkit.getLogger().warning("Session has already stopped");
            return;
        }

        //
        this.currentSession.setSessionEnd(Calendar.getInstance());
        this.currentSession = null;

        //
        isSessionActive = false;
    }

    public ArrayList<Session> getSessions(){
        return this.sessions;
    }

    public void playerDied(PlayerDeathEvent pde) {
        this.currentSession.addEvent(SessionEvents.Player_death, Calendar.getInstance(), pde.getDeathMessage());
    }
}
