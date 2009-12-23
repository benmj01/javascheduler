import ru.scheduler.Task;
import ru.scheduler.Configuration;
import ru.scheduler.ConfigurationFactory;
import ru.scheduler.Scheduler;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 18.12.2009
 * Time: 15:44:12
 */
public class ShedullerTest {
    private Scheduler sched;

    @Before
    public void init() {
        Configuration conf = ConfigurationFactory.getInstance().getConfiguration();
        sched = Scheduler.getInstance();


        Task task;
        task = new Task();
        task.setType(conf.getTasks().get(1));
        task.setParams(new String[] {"thunder*"});
        Calendar c  = Calendar.getInstance();
        c.add(Calendar.SECOND, 20);
        task.setDate(c.getTime());
        sched.addTask(task);

        task = new Task();
        task.setType(conf.getTasks().get(1));
        task.setParams(new String[] {"task*"});
        c.add(Calendar.SECOND, -10);
        task.setDate(c.getTime());
        sched.addTask(task);
    }

    @Test
    public void execute() {
        try {
            Thread.sleep(30 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void destroy() {
        sched.stop();
    }
}
