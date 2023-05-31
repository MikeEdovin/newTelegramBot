package States;

import BotPackage.Bot;
import BotServices.Emojies;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.User;
import MessageCreator.StateMessageBuilder;
import MessageCreator.SystemMessage;
import Service.UserService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class NotificationsState implements State{

    @Autowired
    Bot bot;
    @Autowired
    UserService userService;
    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();

        switch (command) {
            case SET_NOTIFICATIONS_CITY -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.SETTINGS);
                user = userService.update(user);
                sendStateMessage(user, user.getCurrentState());
            }
            case SET_NOTIFICATIONS_DAY_AND_TIME -> {
                bot.sendQueue.add(new SystemMessage.MessageBuilder(user.getUserId()).sendDayTimeKeyboard(user).build().getSendMessage());
            }
            case SET_TIME -> {
                        try {
                            LocalTime time = LocalTime.parse(parsedCommand.getText(), DateTimeFormatter.ofPattern("H[H]:mm"));
                            user.setNotificationTime(time);
                            userService.update(user);
                            System.out.println("Time parsed ok");
                        }catch(DateTimeParseException e){
                            bot.sendQueue.add(new SystemMessage.MessageBuilder(user.getUserId())
                                    .setText(Command.WRONG_TIME_INPUT).build().getSendMessage());
                        }



            }
            case BACK -> {
                user.setCurrentState(StateEnum.MAIN);
                user.setNotif(false);
                userService.update(user);
                sendStateMessage(user, user.getCurrentState());
            }
        }
    }

    @Override
    public void sendStateMessage(User user, StateEnum state) {
        bot.sendQueue.add(new StateMessageBuilder.MessageBuilder(user.getUserId()).
                setText(state).
                setKeyBoard(state).build().getSendMessage());
    }

    @Override
    public void gotCallBack(User user, Update update) {
        Message message=update.getCallbackQuery().getMessage();
        String chatID=String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        AnswerCallbackQuery answerCallbackQuery=new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
        int messageID=message.getMessageId();
        CallbackQuery query=update.getCallbackQuery();
        EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
        InlineKeyboardMarkup keyboardMarkup = updateNotifDaysChoosingKeyboard(user,query);
        editMessageReplyMarkup.setReplyMarkup(keyboardMarkup);
        editMessageReplyMarkup.setMessageId(messageID);
        editMessageReplyMarkup.setChatId(chatID);
        try{
            bot.execute(answerCallbackQuery);
            bot.execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public InlineKeyboardMarkup updateNotifDaysChoosingKeyboard(User user, CallbackQuery query)  {
        InlineKeyboardMarkup keyboardMarkup = query.getMessage().getReplyMarkup();
        List<List<InlineKeyboardButton>> keyboard = keyboardMarkup.getKeyboard();
        for (List<InlineKeyboardButton> row : keyboard) {
            for (InlineKeyboardButton button : row) {
                if (Objects.equals(button.getCallbackData(), query.getData()) &&!button.getText().contains(Emojies.DONE.getEmoji())) {
                    button.setText(button.getText() + " " + Emojies.DONE.getEmoji());
                    user.addNotificationDay(Integer.parseInt(query.getData()));
                } else if (Objects.equals(button.getCallbackData(), query.getData()) &&
                        button.getText().contains(Emojies.DONE.getEmoji())) {
                    button.setText(EmojiParser.removeAllEmojis(button.getText()));
                    user.deleteNotificationDay(Integer.parseInt(query.getData()));
                }
            }
        }
        return keyboardMarkup;
    }
}
