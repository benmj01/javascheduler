package ru.scheduler.view;

import ru.scheduler.Scheduler;
import ru.scheduler.Task;
import ru.scheduler.ConfigurationFactory;
import ru.scheduler.Configuration;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.Date;
import java.util.TimeZone;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 18.12.2009
 * Time: 17:20:56
 */
public class MainFrame extends JFrame {
    private static final Log log = LogFactory.getLog(MainFrame.class);
    private final Timer timer;
    private final TaskDialog dlg;

    public MainFrame() {
        super("Планировщик");
        super.setMinimumSize(new Dimension(400, 160));
        final JFrame window = this;
        dlg = new TaskDialog(this);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel content = new JPanel();
        content.setLayout(gbl);
//        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        final JTable table = new TaskTable();
        final JScrollPane pane = new JScrollPane(table);
        content.add(pane, gbc);

        final JButton addTaskButton = new JButton("Добавить");
        final JButton removeTaskButton = new JButton("Удалить");
        removeTaskButton.setEnabled(false);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        content.add(addTaskButton, gbc);
        content.add(removeTaskButton, gbc);

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (table.isVisible()) {
                    table.repaint();
                }
            }
        });

        addTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                dlg.setVisible(true);
            }
        });

        removeTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Scheduler s = Scheduler.getInstance();
                s.removeTask(s.getTasks().get(table.getSelectedRow()));
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                int index = event.getFirstIndex();
                removeTaskButton.setEnabled(index >= 0);
            }
        });

        try {
            Image icon = ImageIO.read(MainFrame.class.getClassLoader().getResourceAsStream("icon.gif"));
            final TrayIcon trayIcon = new TrayIcon(icon, "Планировщик");
            trayIcon.setImageAutoSize(true);

            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    if (event.getClickCount() >= 2) {
                        window.setVisible(true);
                        window.setState(JFrame.NORMAL);
                        SystemTray.getSystemTray().remove(trayIcon);
                    }
                }
            });
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent windowEvent) {
                    timer.stop();
                    Scheduler.getInstance().stop();
                }

                @Override
                public void windowIconified(WindowEvent windowEvent) {
                    try {
                        window.setVisible(false);
                        SystemTray.getSystemTray().add(trayIcon);
                        trayIcon.displayMessage("Сообщение", "Планировщик работает в фоновом режиме", TrayIcon.MessageType.INFO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            log.error("system tray is not supported or icon not found: " + e.getMessage());
        }

        setContentPane(content);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        timer.start();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = getPreferredSize();
        setLocation((screenSize.width - windowSize.width) / 2, (screenSize.height - windowSize.height) / 2);
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+00:00"));
        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.setLookAndFeel("com.incors.plaf.alloy.AlloyDefault");
//            UIManager.setLookAndFeel("com.incors.plaf.alloy.AlloyBedouin");
//            UIManager.setLookAndFeel("javax.swing.plaf.multi.MultiLookAndFeel");
//            UIManager.setLookAndFeel("javax.swing.plaf.synth.SynthLookAndFeel");
        } catch (Exception e) {
            //do nothing
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
