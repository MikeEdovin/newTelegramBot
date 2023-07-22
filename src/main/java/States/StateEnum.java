package States;

import java.io.Serializable;

public enum StateEnum implements Serializable {
    MAIN("main"),
    SETTINGS("settings"),
    NEWINPUT("newInput"),
    NOTIF("notif");
    public final String description;
    StateEnum(String description) {
        this.description = description;
    }
}
