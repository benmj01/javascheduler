package ru.scheduler;

import java.util.Date;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;
import java.text.MessageFormat;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 18.12.2009
 * Time: 13:58:56
 */
public class Task implements Comparable {
    private TaskType type;
    private Date date;
    private String[] params;

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public void execute() throws IOException {
        final String cmd = MessageFormat.format(type.getCommand(), params);
        System.out.println(cmd);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int compareTo(Object o) {
        if (o == this) {
            return 0;
        } else if (o instanceof Task) {
            return date.compareTo(((Task) o).date);
        } else {
            return -1;
        }
    }
}
