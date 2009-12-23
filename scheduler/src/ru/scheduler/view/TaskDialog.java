package ru.scheduler.view;

import ru.scheduler.*;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 18.12.2009
 * Time: 18:18:16
 */
public class TaskDialog extends JDialog {
    private final JComboBox type;
    private final JSpinner time;
    private final JPanel paramsPanel;


    public TaskDialog(JFrame parent) {
        super(parent, "Добавление задания", true);
        final JDialog dlg = this;
        Configuration conf = ConfigurationFactory.getInstance().getConfiguration();

        paramsPanel = new JPanel();
        paramsPanel.setBorder(BorderFactory.createTitledBorder("Параметры"));
        paramsPanel.setLayout(new GridBagLayout());
//        paramsPanel.setMinimumSize(new Dimension(300, 100));
        type = new JComboBox();

        Calendar c = Calendar.getInstance();
        Date from = new Date(0);
        c.setTime(from);
        c.add(Calendar.DAY_OF_YEAR, 30);
        Date to = c.getTime();

        time = new JSpinner(new SpinnerDateModel(from, from, to, Calendar.SECOND));
        time.setEditor(new JSpinner.DateEditor(time, "HH:mm:ss"));

        JPanel taskPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        taskPanel.setLayout(gbl);
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        final GridBagConstraints labelGbc = (GridBagConstraints) gbc.clone();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        final GridBagConstraints inputGbc = (GridBagConstraints) gbc.clone();

        type.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                TaskType tt = (TaskType) type.getSelectedItem();
                paramsPanel.removeAll();
                if (tt.getParams() != null) {
                    for (Param p : tt.getParams()) {
                        paramsPanel.add(new JLabel(p.getTitle()), labelGbc);
                        paramsPanel.add(new JTextField(), inputGbc);
                    }
                }
                paramsPanel.setVisible(false);
                paramsPanel.setVisible(true);
            }
        });
        for (TaskType t : conf.getTasks()) {
            type.addItem(t);
        }

        taskPanel.add(new JLabel("Тип задачи"), labelGbc);
        taskPanel.add(type, inputGbc);

        taskPanel.add(new JLabel("Интервал"), labelGbc);
        taskPanel.add(time, inputGbc);

        setMinimumSize(new Dimension(500, 180));

        JButton addTask = new JButton("Создать");
        JButton cancel = new JButton("Отмена");

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                dlg.setVisible(false);
            }
        });
        cancel.setMaximumSize(cancel.getPreferredSize());

        addTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Task t = new Task();
                t.setType(getTaskType());
                t.setDate(getTaskDate());
                t.setParams(getParams());
                Scheduler.getInstance().addTask(t);
                dlg.setVisible(false);
            }
        });

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, 2));
        pane.add(taskPanel);
        pane.add(paramsPanel);

        JPanel cPane = new JPanel();
        cPane.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        cPane.add(pane, gbc);
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        cPane.add(addTask, gbc);
        cPane.add(cancel, gbc);
        
        setContentPane(cPane);

    }

    public void setVisible(boolean value) {
        if (value) {
            Point location = getParent().getLocation();
            Dimension size = getParent().getSize();
            Dimension windowSize = getSize();
            setLocation(location.x + size.width / 2 - windowSize.width / 2, location.y + size.height / 2 - windowSize.height / 2 );
        }
        super.setVisible(value);
    }

    public String[] getParams() {
        Component[] comps = paramsPanel.getComponents();
        String[] params = new String[comps.length / 2];
        int index = 0;
        for (Component c : comps) {
            if (c instanceof JTextField) {
                params[index++] = ((JTextField) c).getText();
            }
        }
        return params;
    }

    public TaskType getTaskType() {
        return (TaskType) type.getSelectedItem();
    }

    public Date getTaskDate() {
        Date now = new Date();
        Date date = ((SpinnerDateModel)time.getModel()).getDate();
        return new Date(now.getTime() + date.getTime());
    }
}
