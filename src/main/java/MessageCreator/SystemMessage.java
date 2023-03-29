package MessageCreator;

import Commands.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.sql.SQLException;
import java.time.LocalTime;

public class SystemMessage {
    private static final String END_LINE = "\n";
    SendMessage sendMessage;

    private SystemMessage(MessageBuilder builder) {
        super();
        this.sendMessage = builder.sendMessage;

    }

    public static class MessageBuilder {
        SendMessage sendMessage;

        public MessageBuilder(String chatId) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
        }

        public MessageBuilder setText(Command command) {
            switch (command) {
                case HELP -> {
                    String messageText = "*This is help message*" + END_LINE + END_LINE +
                            "[/Start](/Start) - show main menu" + END_LINE +
                            "/Help" + " - show help message" + END_LINE +
                            "/Current_weather" + " - show current weather " + END_LINE +
                            "/Forecast_for_2_days" + " - show weather forecast for 2 days " + END_LINE +
                            "/Forecast_for_7_days" + " days - show weather forecast for 7 days " + END_LINE +
                            "/Notifications " + " - set weather notifications " + END_LINE +
                            "/Settings " + " - show settings " + END_LINE;
                    sendMessage.setText(messageText);
                }
                case WRONG_CITY_INPUT -> sendMessage.setText("Please, type the city name ");
                case WRONG_TIME_INPUT -> sendMessage.setText("Wrong input, please try again");
                case TIME_SETTINGS_ERROR -> sendMessage.setText("At first you need to choose city");
            }
            return this;

        }
    }
}
