package States;

import BotPackage.Bot;
import BotServices.*;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.User;
import MessageCreator.SystemMessage;
import Service.UserService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NotificationsState implements State {

    @Autowired
    Bot bot;
    @Autowired
    UserService userService;
    @Autowired
    MessageSender messageSender;
    @Autowired
    Notifier notifier;

    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();
        switch (command) {
            case SET_NOTIFICATIONS_CITY -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.SETTINGS);
                userService.updateAsync(user);
                messageSender.sendMessageAsync(getStateMessage(user));
                notifier.gotNotifListUpdate(user);
            }
            case SET_NOTIFICATIONS_DAY_AND_TIME -> messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                    .sendDayTimeKeyboard().build().getSendMessage());
            case SET_TIME -> {
                if(user.hasAtLeastOneNotDay()) {
                    try {
                        LocalTime time = LocalTime.parse(parsedCommand.getText(),
                                DateTimeFormatter.ofPattern("H[H]:mm"));
                        user.setNotificationTime(time);
                        userService.updateAsync(user);
                        messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                                 .setText(Command.NOTIF_TIME_WAS_SET).build().getSendMessage());
                        messageSender.sendMessageAsync(getStateMessage(user));
                        notifier.gotNotifListUpdate(user);
                    } catch (DateTimeParseException e) {
                        messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                                .setText(Command.WRONG_TIME_INPUT).build().getSendMessage());
                    }
                }
                else{
                    messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                            .setText(Command.WRONG_TIME_INPUT).build().getSendMessage());
                }
            }
            case NONE ->
                    messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                            .setText(Command.WRONG_TIME_INPUT).build().getSendMessage());
            case RESET_NOTIFICATIONS -> {
                user.clearNotifications();
                userService.updateAsync(user);
                messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                        .setText(Command.RESET_NOTIFICATIONS).build().getSendMessage());
                notifier.gotNotifListUpdate(user);
            }
            case BACK -> {
                user.setCurrentState(StateEnum.MAIN);
                user.setNotif(false);
                userService.updateAsync(user);
                messageSender.sendMessageAsync(getStateMessage(user));
            }
        }
    }



    @Override
    public void gotCallBack(User user, Update update) {
        Message message=update.getCallbackQuery().getMessage();
        AnswerCallbackQuery answerCallbackQuery=new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
        CallbackQuery query=update.getCallbackQuery();
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(message.getChatId());
        editMessageText.setMessageId(message.getMessageId());
        if(Integer.parseInt(query.getData())<0){
            editMessageText.setText("Back");
                user.setCurrentState(StateEnum.NOTIF);
                try{
                    bot.executeAsync(editMessageText);
                } catch (TelegramApiException e) {
                }
            messageSender.sendMessageAsync(getStateMessage(user));
        }else {
            InlineKeyboardMarkup keyboardMarkup = updateNotifDaysChoosingKeyboard(user, query);
            editMessageReplyMarkup.setReplyMarkup(keyboardMarkup);
            editMessageReplyMarkup.setMessageId(message.getMessageId());
            editMessageReplyMarkup.setChatId(message.getChatId());
            try {
                bot.executeAsync(answerCallbackQuery);
                bot.executeAsync(editMessageReplyMarkup);
                userService.updateAsync(user);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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
