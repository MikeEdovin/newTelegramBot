package Commands;

public enum Command {
    NONE(""),
    START("/Start"),
    HELP("/Help"),
    WEATHER_NOW("/Current_weather"),
    FOR_48_HOURS("/Forecast_for_2_days"),
    FOR_7_DAYS("/Forecast_for_7_days"),
    NOTIFICATION("/Notifications"),
    SETTINGS("/Settings"),
    SET_CITY("/Set_city"),
    SEND_LOCATION("/Send_location"),
    CHOOSE_FROM_LAST_THREE("/Choose_from_last_3"),
    BACK("/Back"),
    ADD_CITY_TO_USER("Add city to user"),
    GET_CITY_FROM_INPUT("Get city from input"),
    SET_NOTIFICATION_TIME("Set_notification_time"),
    SEND_TIME_SETTING_MESSAGE("Sending time setting message"),
    RESET_NOTIFICATIONS("Reset_notification_time"),
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
