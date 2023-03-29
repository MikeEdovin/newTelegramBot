package Commands;

public class Parser {
    public static ParsedCommand GetParsedCommand(String text){

        String trimText="";
        if(text!=null){
            trimText=text.trim();
        }
        ParsedCommand result = new ParsedCommand(Command.NONE, trimText);
        for (Command c : Command.values()) {
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

}
