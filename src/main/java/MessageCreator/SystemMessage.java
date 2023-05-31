package MessageCreator;

import BotServices.Emojies;
import Commands.Command;
import Entities.User;
import NotificationsPackage.Days;
import Service.UserService;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;


public class SystemMessage {
    private static final String END_LINE = "\n";
    private SendMessage sendMessage;

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
            sendMessage.setParseMode(ParseMode.MARKDOWN);
        }


        public MessageBuilder setText(Command command) {
            switch (command) {

                case HELP -> {
                    String messageText = "This is help message" + END_LINE + END_LINE +
                            "Start - show main menu" + END_LINE +
                            "Current weather "+ Emojies.CURRENT.getEmoji()
                            + " - show current weather " + END_LINE +
                            "Forecast for "+Emojies.FOR_2_DAYS.getEmoji()+" days "
                            + " - show weather forecast for 2 days " + END_LINE +
                            "Forecast for "+Emojies.FOR_7_DAYS.getEmoji()+" days "
                            + " days - show weather forecast for 7 days " + END_LINE +
                            "Notifications "+Emojies.CLOCK.getEmoji()
                            + " - set weather notifications " + END_LINE +
                            "Settings "+ Emojies.SETTINGS.getEmoji()
                            + " - show settings " + END_LINE;
                    sendMessage.setText(messageText);
                }
                case NONE -> sendMessage.setText("Please, use menu buttons");
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
                    row.add("Current weather "+ Emojies.CURRENT.getEmoji());
                    row.add("Forecast for "+Emojies.FOR_2_DAYS.getEmoji()+" days ");
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("Forecast for "+Emojies.FOR_7_DAYS.getEmoji()+" days ");
                    row.add("Notifications "+Emojies.CLOCK.getEmoji());
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("Settings "+ Emojies.SETTINGS.getEmoji());
                    row.add("Need help "+Emojies.HELP.getEmoji());
                    keyboard.add(row);
                }
                case SETTINGS -> {
                    row.add("Set city "+Emojies.PENCIL.getEmoji());
                    keyboard.add(row);
                    row = new KeyboardRow();
                    KeyboardButton getLocButton =
                            new KeyboardButton("Send location "+Emojies.SEND_LOCATION.getEmoji());
                    getLocButton.setRequestLocation(true);
                    getLocButton.getRequestLocation();
                    row.add(getLocButton);
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add(" Choose from last 3 "+Emojies.LAST_THREE.getEmoji());
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("Back "+Emojies.BACK.getEmoji());
                    keyboard.add(row);
                }
                case NOTIFICATION -> {
                    row.add("Set notification ");
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("Reset notification ");
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("Back "+Emojies.BACK.getEmoji());
                    keyboard.add(row);
                    keyboardMarkup.setOneTimeKeyboard(true);
                }
            }
            keyboardMarkup.setKeyboard(keyboard);
            keyboardMarkup.setResizeKeyboard(true);
            sendMessage.setReplyMarkup(keyboardMarkup);
            return this;
        }

        public MessageBuilder sendDayTimeKeyboard (User user)  {
            sendMessage.setText("Choose days and enter notifications time in hh : mm");
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            List<InlineKeyboardButton> row1 = new ArrayList<>();
            List<InlineKeyboardButton> row2 = new ArrayList<>();
            Days[] days = Days.values();
            for (int i = 0; i < 4; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                if (!user.isNotificationDay(i)) {
                    button.setText(days[i].name());
                } else {
                    button.setText(days[i].name() + " " + Emojies.DONE.getEmoji());
                }
                button.setCallbackData(String.valueOf(days[i].getDay()));
                row1.add(button);
            }
            for (int i = 4; i < 7; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                if (!user.isNotificationDay(i )) {
                    button.setText(days[i].name());

                } else {
                    button.setText(days[i].name() + " " + Emojies.DONE.getEmoji());
                }
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
