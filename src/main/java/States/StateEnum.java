package States;

import java.io.Serializable;

public enum StateEnum implements Serializable {
    MAIN("main"),
    SETTINGS("settings"),
    NEWINPUT("newInput"),
    LAST3("last3"),
    LOCATION("location"),
    NOTIF("notif"),
    SETDAYTIME("setDayTime"),
    RESETNOTIF("resetNotif");

    public final String description;


    StateEnum(String description) {
        this.description = description;
    }
}
