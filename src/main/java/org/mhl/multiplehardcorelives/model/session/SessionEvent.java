package org.mhl.multiplehardcorelives.model.session;

import org.mhl.multiplehardcorelives.model.session.enums.SessionEvents;

import java.util.Calendar;

/**
 * A model for describing an event in a session
 */
public class SessionEvent {
    /**
     * The kind of event
     */
    public final SessionEvents event;

    /**
     * The event's date of happening
     */
    public final Calendar date;

    /**
     * The event's ID
     */
    public final int eventId;

    /**
     * The event's description
     */
    public final String description;

    /**
     * Creates an instance of SessionEvent
     * @param event The kind of event
     * @param date The event's date of happening
     * @param eventId The event's ID
     * @param description The event's description
     */
    public SessionEvent(SessionEvents event, Calendar date, int eventId, String description){
        this.event = event;
        this.date = date;
        this.eventId = eventId;
        this.description = description;
    }
}
