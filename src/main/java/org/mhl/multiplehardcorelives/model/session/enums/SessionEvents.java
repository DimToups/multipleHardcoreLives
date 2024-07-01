package org.mhl.multiplehardcorelives.model.session.enums;

/**
 * An enumeration for every registered kinds of events
 */
public enum SessionEvents {

    /**
     * The event of a player who has done an advancement
     */
    Advancement,

    /**
     * The event of a player dying
     */
    Player_death,

    /**
     * The event of a player being definitively dead
     */
    Player_definitive_death,

    /**
     * The event of a player resurrecting
     */
    Player_resurrection,

    /**
     * The event of a player joining the server
     */
    Player_join,

    /**
     * The event of a player quitting the server
     */
    Player_quit;

    /**
     * Cleans the name of a SessionEvents by removing the underscores (_)
     * @param event The wanted event
     * @return The cleaned name
     */
    public static String cleanName(SessionEvents event){
        return event.name().replace('_', ' ');
    }
}
