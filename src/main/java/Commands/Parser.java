package Commands;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class Parser {


    public static ParsedCommand GetParsedCommand(String text){
        String trimText="";
        if(text!=null){
            trimText= filterEmojies(text).trim();
            System.out.println("trim text "+trimText+ " "+text);

        }
        ParsedCommand result = new ParsedCommand(Command.NONE, trimText);
        for (Command c : Command.values()) {
            //System.out.println("c "+c.description+" "+c.description.length()+" trim "+trimText+" "+trimText.length());
            if (trimText.equalsIgnoreCase(c.description.trim())) {
                result.setCommand(c);
                break;
                /*
            } else if (trimText.contains("Location")) {//check do I really need it
                result.setCommand(Command.ADD_CITY_TO_USER);
                break;

                 */
            } else if (trimText.matches("\\d{1,2}(:|\\s*|\\.*|,*)\\d{2}")) {
                result.setCommand(Command.SET_TIME);
                result.setText(text);

            }
        }
        System.out.println("Command "+result.getCommand()+" text "+result.getText());
        return result;
    }

    private static String filterEmojies(String input){
        int[]filtered=input.codePoints().filter((c)->Character.isLetter(c)
                ||Character.isDigit(c)||Character.isWhitespace(c)).toArray();
        return new String(filtered, 0, filtered.length);
    }

}
