package ru.scheduler.view;

import ru.scheduler.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import static java.awt.event.KeyEvent.*;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 18.12.2009
 * Time: 18:18:16
 */
public class TaskDialog extends JDialog {
    private static final Logger log = Logger.getLogger(TaskDialog.class);
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final char[] keys = "0123456789:\u0008".toCharArray();

    static {
        Arrays.sort(keys);
    }

    private final JComboBox type;
    private final JSpinner time;
    private final JPanel paramsPanel;


    public TaskDialog(JFrame parent) {
        super(parent, "���������� �������", true);
        final JDialog dlg = this;
        Configuration conf = ConfigurationFactory.getInstance().getConfiguration();

        paramsPanel = new JPanel();
        paramsPanel.setBorder(BorderFactory.createTitledBorder("���������"));
        paramsPanel.setLayout(new GridBagLayout());
        type = new JComboBox();

        Calendar c = Calendar.getInstance();
        Date from = new Date(0);
        c.setTime(from);
        c.add(Calendar.DAY_OF_YEAR, 30);
        Date to = c.getTime();

        time = new JSpinner(new SpinnerDateModel(from, from, to, Calendar.SECOND));

        JSpinner.DateEditor editor = new JSpinner.DateEditor(time, TIME_FORMAT);
        editor.getTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char c = keyEvent.getKeyChar();
                if (c == '\n') {
                    createTask();
                } else if (Arrays.binarySearch(keys, c) < 0) {
                    keyEvent.consume();
                }
            }
        });

        time.setEditor(editor);

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
        final KeyListener enterListener = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if (keyEvent.getKeyChar() == '\n') {
                    createTask();
                }
            }
        };
        type.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                TaskType tt = (TaskType) type.getSelectedItem();
                paramsPanel.removeAll();
                if (tt.getParams() != null) {
                    for (Param p : tt.getParams()) {
                        paramsPanel.add(new JLabel(p.getTitle()), labelGbc);
                        JTextField f = new JTextField();
                        f.addKeyListener(enterListener);
                        paramsPanel.add(f, inputGbc);
                    }
                }
                paramsPanel.doLayout();
                paramsPanel.repaint();
            }
        });
        for (TaskType t : conf.getTasks()) {
            type.addItem(t);
        }

        taskPanel.add(new JLabel("��� ������"), labelGbc);
        taskPanel.add(type, inputGbc);

        taskPanel.add(new JLabel("��������"), labelGbc);
        taskPanel.add(time, inputGbc);

        setMinimumSize(new Dimension(520, 180));

        JButton addTask = new JButton("�������");
        JButton cancel = new JButton("������");

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                dlg.setVisible(false);
            }
        });
        cancel.setMaximumSize(cancel.getPreferredSize());

        addTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                createTask();
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

    private void createTask() {
        Task t = new Task();
        t.setType(getTaskType());
        t.setDate(getTaskDate());
        t.setParams(getParams());
        Scheduler.getInstance().addTask(t);
        setVisible(false);
    }

    public void setVisible(boolean value) {
        if (value) {
            Point location = getParent().getLocation();
            Dimension size = getParent().getSize();
            Dimension windowSize = getSize();
            setLocation(location.x + size.width / 2 - windowSize.width / 2, location.y + size.height / 2 - windowSize.height / 2);
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
        Date date = ((SpinnerDateModel) time.getModel()).getDate();
        return new Date(now.getTime() + date.getTime());
    }
}
