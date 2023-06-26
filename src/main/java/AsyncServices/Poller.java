package AsyncServices;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.concurrent.Callable;

public interface Poller extends Callable {

    void gotMessages(List<Update> updates);

}
