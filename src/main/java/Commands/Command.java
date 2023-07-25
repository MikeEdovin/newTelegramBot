package Commands;

public enum Command {
    NONE("None"),
    START("Start"),
    STOP("Stop"),
    HELP("Need help"),
    CURRENT_WEATHER("Current weather"),
    FOR_48_HOURS("Forecast for 2 days"),
    FOR_7_DAYS("Forecast for 7 days"),
    NOTIFICATION("Notifications"),
    SET_NOTIFICATIONS_CITY("Set notifications city"),
    SET_NOTIFICATIONS_DAY_AND_TIME("Set notifications days  time"),
    SETTINGS("Settings"),
    SET_CITY("Set city"),
    SEND_LOCATION("Send location"),
    CHOOSE_FROM_LAST_THREE("Choose from last 3"),
    BACK("Back"),
    RESET_NOTIFICATIONS("Reset notification"),
    SEND_NEW_VERSION_MESSAGE("Sending new version message"),
    SET_TIME("Time"),
    NOTIF_TIME_WAS_SET("Notification time was set"),
    WRONG_TIME_INPUT("Wrong time input"),
    TIME_SETTINGS_ERROR("You didn't choose any day for notifications");

    public final String description;

    Command(String text){
        this.description=text;
    }

}
