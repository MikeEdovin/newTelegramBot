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
    private User user;

    private WeatherMessage(MessageBuilder builder) {
        super();
        this.sendMessage = builder.sendMessage;


    }

    public SendMessage getSendMessage() {
        return this.sendMessage;
    }

    public static class MessageBuilder {
        SendMessage sendMessage;
        User user;

        public MessageBuilder(User user) {
            sendMessage = new SendMessage();
            this.user=user;
            sendMessage.setChatId(user.getUserId());
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
                    System.out.println(current.toString());
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
        public WeatherMessage build() {
            return new WeatherMessage(this);
        }
    }
}




