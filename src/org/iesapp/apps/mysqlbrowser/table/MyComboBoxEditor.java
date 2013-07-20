/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.iesapp.apps.mysqlbrowser.table;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 *
 * @author Josep
 */
public class MyComboBoxEditor extends DefaultCellEditor {
    public MyComboBoxEditor(String[] items) {
        super(new JComboBox(items));
    }

    @Override
    public Object getCellEditorValue() {
        JComboBox mbox = (JComboBox) this.getComponent();
        return mbox.getSelectedItem();
    }

 
}
