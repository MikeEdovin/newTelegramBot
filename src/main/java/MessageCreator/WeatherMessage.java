package MessageCreator;

import BotServices.Emojies;
import Entities.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class WeatherMessage {
    private static final String END_LINE = "\n";
    private SendMessage sendMessage;

    private WeatherMessage(MessageBuilder builder) {
        super();
        this.sendMessage = builder.sendMessage;

    }

    public SendMessage getSendMessage() {
        return this.sendMessage;
    }

    public static class MessageBuilder {
        SendMessage sendMessage;
        public MessageBuilder(long userId) {
            sendMessage = new SendMessage();
            sendMessage.setChatId(userId);
        }

        public MessageBuilder setCityChoosingKeyBoard(CityData[] cities){
            sendMessage.setText("Choose right city");
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            for (CityData item : cities) {
                if (item != null) {
                    row = new KeyboardRow();
                    row.add(item.getName()+" ,"+item.getCountry());
                    keyboard.add(row);
                }
            }
            row = new KeyboardRow();
            row.add("Back" + Emojies.BACK.getEmoji());
            keyboard.add(row);
            keyboardMarkup.setKeyboard(keyboard);
            keyboardMarkup.setResizeKeyboard(true);
            sendMessage.setReplyMarkup(keyboardMarkup);
            return this;
        }

        public MessageBuilder sendInlineCityChoosingKeyboard (CityData[] cities)  {
            sendMessage.setText("Choose right city ");
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            List<InlineKeyboardButton> row;
            for (int i=0;i<cities.length;i++) {
                if (cities[i] != null) {
                    row=new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(cities[i].getName()+", "+cities[i].getCountry());
                    button.setCallbackData(String.valueOf(i));
                    row.add(button);
                    keyboard.add(row);
                }
            }
            keyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(keyboardMarkup);
            return this;
        }




        public MessageBuilder setForecastText(WeatherData weatherData, CityData city, int nrOfDays) {
            StringBuilder text = new StringBuilder();
            switch (nrOfDays) {

                case 1 -> {
                    Current current=weatherData.getCurrent();
                    text.append("Current weather for ").append(city.getName()).append(", ")
                            .append(city.getCountry())
                            .append(END_LINE).append(END_LINE);
                    text.append("Temperature ").append(current.getCurrentTemp()).append(" °C").append(END_LINE);
                    text.append("Feels like temperature ").append(current.getCurrentFeelsLike()).append(" °C").append(END_LINE);
                    text.append("Pressure ").append(current.getCurrentPressure()).append(" hPa").append(END_LINE);
                    text.append("Humidity ").append(current.getCurrentHumidity()).append(" %").append(END_LINE);
                    text.append("Clouds ").append(current.getCurrentClouds()/100).append(" %").append(END_LINE);
                    text.append("Current wind speed ").append(current.getCurrentWindSpeed()).append(END_LINE);
                    text.append("Wind gust ").append(current.getCurrentWindGust()).append(END_LINE);
                    text.append("Wind degree ").append(current.getCurrentWindDegree()).append(END_LINE);
                    sendMessage.setText(text.toString());
                }
                case 2, 7 -> {
                    Daily[] daily= weatherData.getDaily();
                    text.append("Forecast for ").append(city.getName()).append(", ")
                            .append(city.getCountry())
                            .append(END_LINE).append(END_LINE);
                    for (int i = 0; i < nrOfDays; i++) {
                        Daily day=daily[i];
                        Temp temp=day.getTemp();
                        FeelsLike feelsLike=day.getFeelsLike();
                        text.append("Date ").append(day.getDate()).append(END_LINE);
                        text.append("Sunrise ").append(day.getFormattedSunrise()).append(END_LINE);
                        text.append("Sunset ").append(day.getFormattedSunset()).append(END_LINE);
                        text.append("Temperature ").append(temp.getDayTemp()).append(" °C")
                                .append("at morning ").append(temp.getMornTemp()).append(" °C")
                                .append("at evening ").append(temp.getEveTemp()).append(" °C")
                                .append(END_LINE);
                        text.append("Feels like temperature ").append(feelsLike.getDayFeelsLike())
                                .append("at morning ").append(feelsLike.getMornFeelsLike()).append(" °C")
                                .append("at evening ").append(feelsLike.getEveFeelsLike()).append(" °C")
                                .append(END_LINE);
                        text.append("Pressure ").append(day.getPressure()).append(" hPa").append(END_LINE);
                        text.append("Humidity ").append(day.getHumidity()).append(" %").append(END_LINE);
                        text.append("Clouds").append(day.getClouds()).append(END_LINE);
                        text.append("Wind speed ").append(day.getWindSpeed()).append(END_LINE);
                        text.append("Wind gust ").append(day.getWindGust()).append(END_LINE);
                        text.append("Wind degree ").append(day.getWindDegree()).append(END_LINE);
                        text.append("Rain possibility ").append(day.getRain()/100).append(" %").append(END_LINE);

                        }
                    sendMessage.setText(text.toString());
                }
            }
            return this;
        }
        public WeatherMessage build() {
            return new WeatherMessage(this);
        }
    }
}




