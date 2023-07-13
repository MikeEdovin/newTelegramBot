package MessageCreator;

import BotServices.Emojies;
import Entities.User;
import States.StateEnum;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;

public class StateMessage {
    private final SendMessage sendMessage;

    private StateMessage(MessageBuilder builder) {
        super();
        this.sendMessage = builder.sendMessage;
    }
    public SendMessage getSendMessage(){
        return  this.sendMessage;
    }

    public static class MessageBuilder {
        SendMessage sendMessage;
        StateEnum currentState;

        public MessageBuilder(User user) {
            sendMessage = new SendMessage();
            currentState=user.getCurrentState();
            sendMessage.setChatId(user.getUserId());
            sendMessage.setParseMode(ParseMode.MARKDOWN);
        }
        public MessageBuilder setText() {
            switch (currentState) {
                case MAIN -> sendMessage.setText("Main menu");
                case SETTINGS -> sendMessage.setText("Settings");
                case NEWINPUT -> sendMessage.setText("Please, enter city name");
                case NOTIF -> sendMessage.setText("Notifications settings");
            }
            return this;
        }
        public MessageBuilder setKeyBoard() {
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            switch (currentState) {
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
                    keyboardMarkup.setOneTimeKeyboard(false);
                }
            }
            keyboardMarkup.setKeyboard(keyboard);
            keyboardMarkup.setResizeKeyboard(true);
            sendMessage.setReplyMarkup(keyboardMarkup);
            return this;
        }
        public StateMessage build() {
            return new StateMessage(this);
        }
    }
}
