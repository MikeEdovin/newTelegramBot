package Commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {

    final static Logger logger= LoggerFactory.getLogger(Parser.class);
    public static ParsedCommand GetParsedCommand(String text){
        logger.info("Parsing text "+text);
        String trimText="";
        if(text!=null){
            trimText= filterEmojies(text).trim();
        }
        ParsedCommand result = new ParsedCommand(Command.NONE, trimText);
        for (Command c : Command.values()) {
            if (trimText.equalsIgnoreCase(c.description.trim())) {
                result.setCommand(c);
                break;
            } else if (trimText.matches("\\d{1,2}(:|\\s*|\\.*|,*)\\d{2}")) {
                result.setCommand(Command.SET_TIME);
                result.setText(text);
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
