package Logger;

import org.aspectj.lang.annotation.Aspect;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

@Aspect
public class Logger {
    static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("Logger");
    private static FileHandler finerHandler=null;
    private static FileHandler warningHandler=null;

    public static void initLogger(){
        try{
            finerHandler=new FileHandler("Logger_finer.log",false);
            warningHandler=new FileHandler("Logger_warning.log",false);
        }catch(SecurityException | IOException e){
            e.printStackTrace();
        }
        finerHandler.setFormatter(new SimpleFormatter());
        finerHandler.setLevel(Level.FINER);

        warningHandler.setFormatter(new SimpleFormatter());
        warningHandler.setLevel(Level.WARNING);

        logger.addHandler(finerHandler);
        logger.addHandler(warningHandler);

        logger.setLevel(Level.FINER);
    }

}
