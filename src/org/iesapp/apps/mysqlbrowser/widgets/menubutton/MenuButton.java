/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.mysqlbrowser.widgets.menubutton;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Josep
 */
public class MenuButton extends javax.swing.JButton{
    
   
    private JPopupMenu popup;
    private final ArrayList<JMenuItem> listItems;
   
    public MenuButton()
    {
         
        this.setText("Menu");
        this.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                       showpopup(evt);
                    }

                });
       
        listItems = new ArrayList<JMenuItem>();
        
    }
                
    public void registerActionListener(int id, ActionListener listener)
    {
        if(id<listItems.size()) {
            listItems.get(id).addActionListener(listener);
        }
    }
   
    public void setItems(String[] items) {
        popup = new JPopupMenu();
        
        DefaultListModel lstmodel = new DefaultListModel();
        for(int i=0; i<items.length;i++)
        {
            JMenuItem jmitem = new JMenuItem(items[i]);
            listItems.add(jmitem);
            popup.add(jmitem);
        }
        
    }
     
      public void setItems(JLabel[] labels) {
         popup = new JPopupMenu();
         
        DefaultListModel lstmodel = new DefaultListModel();
        for(JLabel label: labels)
        {
            JMenuItem jmitem = new JMenuItem(label.getText());
            jmitem.setIcon(label.getIcon());
            listItems.add(jmitem);
            popup.add(jmitem);
        }        
        
    }

    
    private void showpopup(MouseEvent e) {
        if(this.isEnabled())
        {
            popup.show(this, 0, this.getHeight()+2);         
        }
    }
 
    
 
}
