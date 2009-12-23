package ru.scheduler.view;

import ru.scheduler.Scheduler;
import ru.scheduler.Task;

import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 18.12.2009
 * Time: 16:24:54
 */
public class TableAdapter extends AbstractTableModel implements Observer {
    private static final String[] HEADERS = new String[] {"Задача", "Параметры", "Таймаут"};
    private Scheduler sched = Scheduler.getInstance();

    public TableAdapter() {
        sched.addObserver(this);
    }

    public int getRowCount() {
        return sched.getTaskCount();
    }

    public int getColumnCount() {
        return 3;
    }

    public Object getValueAt(int row, int col) {
        List<Task> tasks = sched.getTasks();
        String value = "unknown";
        if (tasks != null) {
            Task t = tasks.get(row);
            switch (col) {
                case 0: {
                    value = t.getType().getDescription();
                }
                break;
                case 1: {
                    String[] params = t.getParams();
                    StringBuilder sb = new StringBuilder();
                    for (String s : params) {
                        sb.append(s).append(" ");
                    }
                    value = sb.toString();
                }
                break;
                case 2 : {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    value = sdf.format(new Date(t.getDate().getTime() - System.currentTimeMillis()));
                }
                break;
            }
        }
        return value;
    }

    public String getColumnName(int col) {
        return HEADERS[col];
    }

    public void update(Observable observable, Object o) {
        fireTableDataChanged();
    }
}
