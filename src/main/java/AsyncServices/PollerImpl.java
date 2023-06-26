package AsyncServices;

import BotPackage.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class PollerImpl implements Poller{
    @Autowired
    Bot bot;
    @Override
    public void gotMessages(List<Update> updates) {

    }

    @Override
    public Object call() throws Exception {
        return null;
    }
}
