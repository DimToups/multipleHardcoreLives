package org.mhl.multiplehardcorelives.model.enums;

public enum SessionEvents {
    Advancement,
    Player_death,
    Player_definitive_death,
    Player_resurrection,
    Player_join,
    Player_quit;

    public static String cleanName(SessionEvents event){
        return event.name().replace('_', ' ');
    }
}
