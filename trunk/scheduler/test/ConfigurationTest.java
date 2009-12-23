import org.junit.Test;
import ru.scheduler.ConfigurationFactory;
import ru.scheduler.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 27.11.2009
 * Time: 18:30:23
 */
public class ConfigurationTest {
    @Test
    public void load() {
        Configuration c = ConfigurationFactory.getInstance().getConfiguration();
        c.getTasks();
        
    }
}
