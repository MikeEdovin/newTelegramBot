package MessageCreator;

import BotServices.Emojies;
import NotificationsPackage.Days;
import Service.UserService;
import States.StateEnum;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class StateMessageBuilder {
    private static final String END_LINE = "\n";
    private SendMessage sendMessage;

    private StateMessageBuilder(MessageBuilder builder) {
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


        public MessageBuilder setText(StateEnum state) {
            switch (state) {
                case MAIN -> sendMessage.setText("Main menu");
                case SETTINGS -> sendMessage.setText("Settings");
                case NEWINPUT -> sendMessage.setText("Please, enter city name");
                case NOTIF -> sendMessage.setText("Notifications settings");



            }
            return this;

        }
        public MessageBuilder setKeyBoard(StateEnum state) {
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            switch (state) {
                case MAIN -> {
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
                    keyboardMarkup.setOneTimeKeyboard(true);
                }
                case NOTIF -> {
                    row.add("Set notifications city "+Emojies.CITY.getEmoji());
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("Set notifications days & time "+Emojies.CLOCK.getEmoji());
                    keyboard.add(row);
                    row = new KeyboardRow();
                    row.add("Reset notification "+Emojies.ERASER.getEmoji());
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

        public MessageBuilder sendInlineKeyboard (UserService userService)  {
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


        public StateMessageBuilder build() {
            return new StateMessageBuilder(this);
        }

    }
}
