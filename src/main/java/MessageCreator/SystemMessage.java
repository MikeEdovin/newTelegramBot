package MessageCreator;

import Commands.Command;
import NotificationsPackage.Days;
import Service.IUserService;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SystemMessage {
    private static final String END_LINE = "\n";
    SendMessage sendMessage;

    private SystemMessage(MessageBuilder builder) {
        super();
        this.sendMessage = builder.sendMessage;

    }
    public SendMessage getSendMessage(){
        return  this.sendMessage;
    }

    public static class MessageBuilder {
        SendMessage sendMessage;


        public MessageBuilder(long userId) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(userId);
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
                            "/Notifications" + " - set weather notifications " + END_LINE +
                            "/Settings" + " - show settings " + END_LINE;
                    sendMessage.setText(messageText);
                }
                case WRONG_CITY_INPUT -> sendMessage.setText("Please, type the city name ");
                case WRONG_TIME_INPUT -> sendMessage.setText("Wrong input, please try again");
                case TIME_SETTINGS_ERROR -> sendMessage.setText("At first you need to choose city");
            }
            return this;

        }
        public MessageBuilder setKeyBoard(Command command) {
            sendMessage.setText(command.description);
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            switch (command) {
                case START -> {
                    row.add("/Current_weather");
                    row.add("/Forecast_for_2_days");
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("/Forecast_for_7_days");
                    row.add("/Notifications");
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("/Settings");
                    row.add("/Help");
                    keyboard.add(row);
                }
                case SET_CITY -> {
                    row.add("/Set_city");
                    keyboard.add(row);
                    row = new KeyboardRow();
                    KeyboardButton getLocButton = new KeyboardButton("/Send_location");
                    getLocButton.setRequestLocation(true);
                    getLocButton.getRequestLocation();
                    row.add(getLocButton);
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("/Choose_from_last_3");
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("/Back");
                    keyboard.add(row);
                }
                case NOTIFICATION -> {
                    row.add("Set_notification_time");
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("Reset_notification_time");
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("/Back");
                    keyboard.add(row);
                    keyboardMarkup.setOneTimeKeyboard(true);
                }
            }
            keyboardMarkup.setKeyboard(keyboard);
            keyboardMarkup.setResizeKeyboard(true);
            sendMessage.setReplyMarkup(keyboardMarkup);
            return this;
        }

        public MessageBuilder sendInlineKeyboard (IUserService userService)  {
            sendMessage.setText("Choose days and enter notifications time in hh : mm");
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            List<InlineKeyboardButton> row1 = new ArrayList<>();
            List<InlineKeyboardButton> row2 = new ArrayList<>();
            Days[] days = Days.values();
            for (int i = 0; i < 4; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
               // if (!daoInterface.isNotificationDay(i + 1, Long.parseLong(this.sendMessage.getChatId()))) {
                    button.setText(days[i].name());
                    /*
                } else {
                    button.setText(days[i].name() + " " + Emojies.DONE.getEmoji());

                }

                     */
                button.setCallbackData(String.valueOf(days[i].getDay()));
                row1.add(button);
            }
            for (int i = 4; i < 7; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
               // if (!daoInterface.isNotificationDay(i + 1, Long.parseLong(this.sendMessage.getChatId()))) {
                    button.setText(days[i].name());
                    /*
                } else {
                    button.setText(days[i].name() + " " + Emojies.DONE.getEmoji());
                }

                     */

                button.setCallbackData(String.valueOf(days[i].getDay()));
                row2.add(button);
            }
            keyboard.add(row1);
            keyboard.add(row2);
            keyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(keyboardMarkup);
            return this;
        }


        public SystemMessage build() {
            return new SystemMessage(this);
        }

    }
}
