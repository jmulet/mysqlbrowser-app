/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.mysqlbrowser;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.iesapp.database.MyDatabase;


/**
 *
 * @author Josep
 */
public class DBEditorDlg extends JDialog {
    private final DBEditorPanel panel;
    
    public DBEditorDlg(Frame parent, boolean modal, String title, MyDatabase mysql, String dbName, String table)
    {
        super(parent, modal);
        this.setTitle(title);
        
        panel = new DBEditorPanel();
        panel.setTable(mysql, dbName, table);
        this.add(panel);
        this.pack();
    }
    
    public DBEditorPanel getDBEditorPanel()
    {
        return panel;
    }
    
    //Enable ESCAPE
    
    @Override
    protected JRootPane createRootPane() {
    JRootPane rootPane2 = new JRootPane();
    KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
    AbstractAction actionListener = new AbstractAction() {
     @Override
      public void actionPerformed(ActionEvent actionEvent) {
        setVisible(false);
      }
    } ;
    InputMap inputMap = rootPane2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    inputMap.put(stroke, "ESCAPE");
    rootPane2.getActionMap().put("ESCAPE", actionListener);

    return rootPane2;
  }
}
