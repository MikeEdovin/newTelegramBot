package MessageCreator;

import BotServices.Emojies;
import Commands.Command;
import Entities.*;
import NotificationsPackage.Days;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
        User user;


        public MessageBuilder(User user) {
            sendMessage = new SendMessage();
            this.user=user;
            sendMessage.setChatId(user.getUserId());
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
                case NONE,SET_TIME -> sendMessage.setText("Please, use menu buttons");
                case NOTIF_TIME_WAS_SET -> {
                    Days[]days=Days.values();
                    StringBuilder builder = new StringBuilder();
                    for(Days day:days){
                        if (user.isNotificationDay(day.getDay())) {
                            builder.append(day.name()).append(" ");
                    }
                        if(user.getNotificationCity()!=null){
                            CityData notifCity=user.getNotificationCity();
                            sendMessage.setText("Notifications time was set for "
                                    +notifCity.getName()+", "+notifCity.getCountry()
                                    +" at "+builder+" "+user.getNotificationTime());
                        }
                        else{
                            sendMessage.setText("Notifications time was set for "
                                    +builder+" at "+user.getNotificationTime()
                                    +". Don't forget to set notifications City");
                        }
                }
                }
                case WRONG_TIME_INPUT -> sendMessage.setText("Incorrect time input. Please try again");
                case TIME_SETTINGS_ERROR -> sendMessage.setText("You didn't choose any day for notifications");
                case RESET_NOTIFICATIONS -> sendMessage.setText("Notifications parameters were reset");

            }
            return this;

        }
        public MessageBuilder sendDayTimeKeyboard ()  {
            sendMessage.setText("Choose days and enter notifications time in hh : mm");
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            List<InlineKeyboardButton> row1 = new ArrayList<>();
            List<InlineKeyboardButton> row2 = new ArrayList<>();
            List<InlineKeyboardButton> row3 = new ArrayList<>();
            Days[] days = Days.values();
            for (int i = 1; i <= 4; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                if (!user.isNotificationDay(i)) {
                    button.setText(days[i-1].name());
                } else {
                    button.setText(days[i-1].name() + " " + Emojies.DONE.getEmoji());
                }
                button.setCallbackData(String.valueOf(days[i-1].getDay()));
                row1.add(button);
            }
            for (int i = 5; i <= 7; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                if (!user.isNotificationDay(i)) {
                    button.setText(days[i-1].name());

                } else {
                    button.setText(days[i-1].name() + " " + Emojies.DONE.getEmoji());
                }
                button.setCallbackData(String.valueOf(days[i-1].getDay()));
                row2.add(button);
            }
            InlineKeyboardButton button=new InlineKeyboardButton();
            button.setText("Back "+Emojies.BACK.getEmoji());
            button.setCallbackData("-1");
            row3.add(button);
            keyboard.add(row1);
            keyboard.add(row2);
            keyboard.add(row3);
            keyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(keyboardMarkup);
            return this;
        }
        public MessageBuilder sendInlineCityChoosingKeyboard (List<CityData>cities)  {
            sendMessage.setText("Which one did you mean? ");
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            List<InlineKeyboardButton> row;
            for (int i=0;i<cities.size();i++) {
                if (cities.get(i) != null) {
                    row=new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(cities.get(i).getName()+", "+cities.get(i).getCountry());
                    button.setCallbackData(String.valueOf(i));
                    row.add(button);
                    keyboard.add(row);
                }
            }
            row=new ArrayList<>();
            InlineKeyboardButton button=new InlineKeyboardButton();
            button.setText("Back "+Emojies.BACK.getEmoji());
            button.setCallbackData("-1");
            row.add(button);
            keyboard.add(row);
            keyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(keyboardMarkup);
            return this;
        }
        public MessageBuilder setCityWasSetText(CityData city, boolean isNotif){
            if(isNotif){
                sendMessage.setText("Notifications city was set to "+city.getName()+", "+city.getCountry());
            }
            else{
                sendMessage.setText("Current city was set to "+city.getName()+", "+city.getCountry());
            }

            return this;
        }
        public MessageBuilder noCurrentCity(){
            sendMessage.setText("Please, set City");
            return this;
        }
        public MessageBuilder setForecastText(WeatherData weatherData, CityData city, int nrOfDays) {
            StringBuilder text = new StringBuilder();
            switch (nrOfDays) {
                case 1 -> {
                    Current current=weatherData.getCurrent();
                    text.append("Current weather in ").append(city.getName()).append(", ")
                            .append(city.getCountry())
                            .append(END_LINE).append(END_LINE);
                    text.append("Temperature ").append(current.getCurrentTemp()).append(" °C").append(END_LINE);
                    text.append("Feels like temperature ").append(current.getCurrentFeelsLike()).append(" °C").append(END_LINE);
                    text.append("Pressure ").append(current.getCurrentPressure()).append(" hPa").append(END_LINE);
                    text.append("Humidity ").append(current.getCurrentHumidity()).append(" %").append(END_LINE);
                    text.append("Clouds ").append(current.getCurrentClouds()).append(" %").append(END_LINE);
                    text.append("UVI ").append(current.getCurrentUvi()).append(END_LINE);
                    text.append("Wind speed ").append(current.getCurrentWindSpeed()).append(END_LINE);
                    text.append("Wind direction ").append(current.getWindDirection()).append(END_LINE);
                    sendMessage.setText(text.toString());
                }
                case 2, 7 -> {
                    Daily[] daily= weatherData.getDaily();
                    text.append("Forecast in ").append(city.getName()).append(", ")
                            .append(city.getCountry())
                            .append(END_LINE).append(END_LINE);
                    for (int i = 0; i < nrOfDays; i++) {
                        Daily day=daily[i];
                        Temp temp=day.getTemp();
                        FeelsLike feelsLike=day.getFeelsLike();
                        text.append("Date ").append(day.getDate()).append(END_LINE);
                        text.append("Sunrise ").append(day.getFormattedSunrise()).append(END_LINE);
                        text.append("Sunset ").append(day.getFormattedSunset()).append(END_LINE);
                        text.append("Temperature ").append(temp.getDayTemp()).append(" °C").append(END_LINE)
                                .append("at the morning ").append(temp.getMornTemp()).append(" °C").append(END_LINE)
                                .append("at the evening ").append(temp.getEveTemp()).append(" °C")
                                .append(END_LINE);
                        text.append("Feels like temperature ").append(feelsLike.getDayFeelsLike()).append(END_LINE)
                                .append("at the morning ").append(feelsLike.getMornFeelsLike()).append(" °C").append(END_LINE)
                                .append("at the evening ").append(feelsLike.getEveFeelsLike()).append(" °C")
                                .append(END_LINE);
                        text.append("Pressure ").append(day.getPressure()).append(" hPa").append(END_LINE);
                        text.append("Humidity ").append(day.getHumidity()).append(" %").append(END_LINE);
                        text.append("Clouds").append(day.getClouds()).append(END_LINE);
                        text.append("Wind speed ").append(day.getWindSpeed()).append(END_LINE);
                        text.append("Wind gust ").append(day.getWindGust()).append(END_LINE);
                        text.append("Wind direction ").append(day.getWindDirection())
                                .append(END_LINE).append(END_LINE);
                    }
                    sendMessage.setText(text.toString());
                }
            }
            return this;
        }

        public SystemMessage build() {
            return new SystemMessage(this);
        }

    }
}
