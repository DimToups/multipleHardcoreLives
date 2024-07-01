package org.mhl.multiplehardcorelives.model.enums;

public enum SessionEvents {
    Advancement,
    Player_death,
    Player_definitive_death,
    Player_resurrection;

    public static String cleanName(SessionEvents event){
        return event.name().replace('_', ' ');
    }
}
