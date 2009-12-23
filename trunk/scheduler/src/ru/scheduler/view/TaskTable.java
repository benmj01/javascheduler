package ru.scheduler.view;

import javax.swing.table.TableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 18.12.2009
 * Time: 17:16:21
 */
public class TaskTable extends JTable {
    private static final int MAX_COLUMN_WIDTH = 500;
    private static final int COLUMN_HEADER_INSET = 20;
    private static final int COLUMN_DATA_INSET = 10;
    private static final int COMPUTE_COLUMN = 10;

    public TaskTable() {
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setModel(new TableAdapter());
//        this.resizeColumns();
    }

    private void resizeColumns() {
        FontMetrics fm = this.getFontMetrics(this.getFont());
        TableModel ta = this.getModel();
        TableColumnModel cmodel = this.getColumnModel();
        for (int i = 0; i < ta.getColumnCount(); i++) {
            TableColumn col = cmodel.getColumn(i);
            String name = ta.getColumnName(i);
            int sWidth = fm.stringWidth(name);
            if (col.getWidth() - COLUMN_HEADER_INSET < sWidth && sWidth < MAX_COLUMN_WIDTH) {
                col.setPreferredWidth(sWidth + COLUMN_HEADER_INSET);
                col.setWidth(sWidth + COLUMN_HEADER_INSET);
            }
        }

        for (int i = 0; i < Math.min(COMPUTE_COLUMN, ta.getRowCount()); i++) {
            for (int j = 0; j < ta.getColumnCount(); j++) {
                TableColumn col = cmodel.getColumn(j);
                String data = ta.getValueAt(i, j).toString();
                if (data != null) {
                    int sWidth = fm.stringWidth(data);
                    if (col.getWidth() - COLUMN_DATA_INSET < sWidth && sWidth < MAX_COLUMN_WIDTH) {
                        col.setPreferredWidth(sWidth + COLUMN_DATA_INSET);
                        col.setWidth(sWidth + COLUMN_DATA_INSET);
                    }
                }
            }
        }
    }
}
