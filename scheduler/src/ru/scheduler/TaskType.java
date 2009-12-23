package ru.scheduler;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 27.11.2009
 * Time: 17:51:38
 */

@XmlType(name = "type")
public class TaskType {
    private Date date;
    private String description;
    private String command;

    private List<Param> params;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @XmlElement(name = "param")
    @XmlElementWrapper(name = "params")
    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public String toString() {
        return description;
    }
}
