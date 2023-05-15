package Commands;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiParser;

public class Parser {

    public static Command getCommand(String text) {
        String trimText = "";
        if (text != null) {
            trimText = filterEmojies(text).trim();
            for (Command c : Command.values()) {
                if (trimText.equalsIgnoreCase(c.description.trim())) {
                    return c;
                }
            }
        }
        return Command.NONE;
    }
    public static ParsedCommand GetParsedCommand(String text){
        String trimText="";
        if(text!=null){
            trimText= filterEmojies(text).trim();

        }
        ParsedCommand result = new ParsedCommand(Command.NONE, trimText);
        for (Command c : Command.values()) {
            System.out.println("c "+c.description+" "+c.description.length()+" trim "+trimText+" "+trimText.length());
            if (trimText.equalsIgnoreCase(c.description.trim())) {
                result.setCommand(c);
                break;
            } else if (trimText.contains("Location")) {
                result.setCommand(Command.ADD_CITY_TO_USER);
                break;
            }else if(trimText.matches("\\d{1,2}(:|\\s*|\\.*|,*)\\d{2}")){
                result.setCommand(Command.SET_TIME);
                StringBuilder builder = new StringBuilder();
                String[] parts = trimText.split("[ ,.:]");
                if (parts[0].length() == 1) {
                    builder.append("0").append(parts[0]);
                } else {
                    builder.append(parts[0]);
                }
                builder.append(":").append(parts[1]);
                result.setText(builder.toString());
            } else {
                result.setCommand(Command.GET_CITY_FROM_INPUT);
                result.setText(trimText);
            }
        }
        return result;
    }

    private static String filterEmojies(String input){
        int[]filtered=input.codePoints().filter((c)->Character.isLetter(c)
                ||Character.isDigit(c)||Character.isWhitespace(c)).toArray();
        return new String(filtered, 0, filtered.length);
    }

}
