import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import ru.scheduler.Task;
import ru.scheduler.TaskType;
import ru.scheduler.ConfigurationFactory;
import ru.scheduler.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 18.12.2009
 * Time: 14:04:21
 */

public class TaskTest {

    private Task task;

    @Before
    public void init() {
        Configuration conf = ConfigurationFactory.getInstance().getConfiguration();

        task = new Task();
        task.setType(conf.getTasks().get(1));
        task.setParams(new String[] {"spool*"});
    }

    @Test
    public void execute() {
        try {
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void destroy() {
        task = null;
    }
}
