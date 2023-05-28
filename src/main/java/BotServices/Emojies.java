package BotServices;


public enum Emojies {
    PARTLY_SUNNY("⛅"),
    SUNNY("☀️"),
    CLOUDY("☁️"),
    RAINY("\uD83C\uDF27️"),
    SNOWLY("\uD83C\uDF28️"),
    WIND("\uD83C\uDF2C️"),
    FOR_2_DAYS("2️⃣"),
    FOR_7_DAYS("7️⃣"),
    CLOCK("⏰"),
    SETTINGS("⚙️"),
    ERASER("\uD83E\uDDFD"),
    HELP("❓"),
    PENCIL("✏"),
    CITY("\uD83C\uDFD9️"),
    TEMPERATURE("\uD83C\uDF21"),
    SEND_LOCATION("\uD83D\uDCCD"),
    LAST_THREE("\uD83E\uDDFE"),
    BACK("↩"),
    DATE("\uD83D\uDCC6"),
    CURRENT("\uD83E\uDE9F"),
    PRESSURE("\uD83C\uDF43"),
    HUMIDITY("\uD83C\uDF2B️"),
    DONE("✅"),
    ;
    final String emojiUnicode;
    Emojies(String code){
        this.emojiUnicode=code;
    }
    public String getEmoji(){
        return this.emojiUnicode;
    }

}
