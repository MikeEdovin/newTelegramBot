package Commands;

public enum Command {
    NONE("None"),
    START("Start"),
    HELP("Need help"),
    CURRENT_WEATHER("Current weather"),
    FOR_48_HOURS("Forecast for 2 days"),
    FOR_7_DAYS("Forecast for 7 days"),
    NOTIFICATION("Notifications"),
    SET_NOTIFICATION("Set notification"),
    SETTINGS("Settings"),
    SET_CITY("Set city"),
    SEND_LOCATION("Send location"),
    CHOOSE_FROM_LAST_THREE("Choose from last 3"),
    BACK("Back"),
    ADD_CITY_TO_USER("Add city to user"),
    GET_CITY_FROM_INPUT("Get city from input"),
    SET_NOTIFICATION_TIME("Set notification"),
    SEND_TIME_SETTING_MESSAGE("Sending time setting message"),
    RESET_NOTIFICATIONS("Reset notification"),
    SEND_NEW_VERSION_MESSAGE("Sending new version message"),
    SET_TIME("Time"),
    WRONG_TIME_INPUT("Wrong time input"),
    WRONG_CITY_INPUT("Wrong input, please type the city name"),
    TIME_SETTINGS_ERROR("At first you need to choose city");

    public final String description;

    Command(String text){
        this.description=text;
    }

}
