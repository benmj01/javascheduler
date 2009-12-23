package ru.scheduler;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 27.11.2009
 * Time: 18:22:28
 */
@XmlRootElement(name = "configuration")
public class Configuration {
    
    private List<TaskType> tasks;

    @XmlElement(name = "type")
    public List<TaskType> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskType> tasks) {
        this.tasks = tasks;
    }
}
