package org.iesapp.apps.mysqlbrowser;


import java.sql.SQLException;
import javax.swing.JInternalFrame;
import org.iesapp.database.MyConnectionBean;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Josep
 */
public class MysqlConJInternalFrame extends JInternalFrame {
    
    private MysqlConJPanel mysqlConJPanel;
    
    public MysqlConJInternalFrame(MyConnectionBean conBean) throws SQLException
    {
        mysqlConJPanel = new MysqlConJPanel();
        mysqlConJPanel.setConBean(conBean);
        this.add(mysqlConJPanel);
        this.pack();
    }

    public MysqlConJPanel getMysqlConJPanel() {
        return mysqlConJPanel;
    }

    public void setMysqlConJPanel(MysqlConJPanel mysqlConJPanel) {
        this.mysqlConJPanel = mysqlConJPanel;
    }
}
