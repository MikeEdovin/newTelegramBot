package States;

import BotPackage.Bot;
import BotServices.*;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.User;
import MessageCreator.SystemMessage;
import Service.UserService;
import com.vdurmont.emoji.EmojiParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class NotificationsState implements State {

    @Autowired
    Bot bot;
    @Autowired
    UserService userService;
    @Autowired
    Notifier notifier;
    @Value("${bot.admin}") long botAdmin;
    final static Logger logger= LoggerFactory.getLogger(NotificationsState.class);

    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) throws TelegramApiException {
        Command command = parsedCommand.getCommand();
        logger.info("Got message from user with id "+user.getUserId()+". Command: "+command);
        switch (command) {
            case SET_NOTIFICATIONS_CITY -> {
                try {
                    user.setPreviousState(user.getCurrentState());
                    user.setCurrentState(StateEnum.SETTINGS);
                    user = userService.updateAsync(user).get();
                    bot.executeAsync(getStateMessage(user));
                }
                catch (ExecutionException|InterruptedException e){
                    logger.warn(e.getMessage());
                }
            }
            case SET_NOTIFICATIONS_DAY_AND_TIME -> bot.executeAsync(new SystemMessage.MessageBuilder(user)
                    .sendDayTimeKeyboard().build().getSendMessage());
            case SET_TIME -> {
                if(user.hasAtLeastOneNotDay()) {
                    try {
                        LocalTime time = LocalTime.parse(parsedCommand.getText(),
                                DateTimeFormatter.ofPattern("H[H]:mm"));
                        user.setNotificationTime(time);
                        user=userService.updateAsync(user).get();
                        bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                 .setText(Command.NOTIF_TIME_WAS_SET).build().getSendMessage());
                        bot.executeAsync(getStateMessage(user));
                        notifier.gotNotifListUpdate();
                    } catch (DateTimeParseException e) {
                        bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                .setText(Command.WRONG_TIME_INPUT).build().getSendMessage());
                    } catch (ExecutionException|InterruptedException e) {
                        logger.warn(e.getMessage());
                    }
                }
                else{
                    bot.executeAsync(new SystemMessage.MessageBuilder(user)
                            .setText(Command.WRONG_TIME_INPUT).build().getSendMessage());
                }
            }
            case NONE ->
                    bot.executeAsync(new SystemMessage.MessageBuilder(user)
                            .setText(Command.WRONG_TIME_INPUT).build().getSendMessage());
            case RESET_NOTIFICATIONS -> {
                user.clearNotifications();
                CompletableFuture<User> futureUser=userService.updateAsync(user);
                try {
                    futureUser.get();
                    bot.executeAsync(new SystemMessage.MessageBuilder(user)
                            .setText(Command.RESET_NOTIFICATIONS).build().getSendMessage());
                    notifier.gotNotifListUpdate();
                }catch (InterruptedException| ExecutionException e){
                    logger.warn(e.getMessage());
                    bot.executeAsync(new SystemMessage.MessageBuilder(new User(botAdmin))
                            .sendErrorMessage(e.getMessage()).build().getSendMessage());
                }
            }
            case BACK,START -> {
                user.setCurrentState(StateEnum.MAIN);
                user.setNotif(false);
                try {
                    user = userService.updateAsync(user).get();
                    bot.executeAsync(getStateMessage(user));
                }catch (ExecutionException|InterruptedException e){
                    logger.warn(e.getMessage());
                }
            }
        }
    }

    @Override
    public void gotCallBack(User user, Update update) throws TelegramApiException {
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
                    bot.execute(editMessageText);
                } catch (TelegramApiException e) {
                    logger.warn(e.getMessage());
                }
            bot.executeAsync(getStateMessage(user));
        }else {
            InlineKeyboardMarkup keyboardMarkup = updateNotifDaysChoosingKeyboard(user, query);
            editMessageReplyMarkup.setReplyMarkup(keyboardMarkup);
            editMessageReplyMarkup.setMessageId(message.getMessageId());
            editMessageReplyMarkup.setChatId(message.getChatId());
            try {
                bot.execute(answerCallbackQuery);
                bot.execute(editMessageReplyMarkup);
                userService.updateAsync(user);
            } catch (TelegramApiException e) {
                logger.warn(e.getMessage());
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
