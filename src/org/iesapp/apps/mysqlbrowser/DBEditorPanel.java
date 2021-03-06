package org.iesapp.apps.mysqlbrowser;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DBEditorPanel.java
 *
 * Created on 25-feb-2012, 13:14:54
 */


 
 
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.iesapp.apps.mysqlbrowser.table.LongText;
import org.iesapp.apps.mysqlbrowser.wizard.ExcelImportWiz;
import org.iesapp.database.MyDatabase;
import org.iesapp.util.StringUtils;

/**
 *
 * @author Josep
 */
public class DBEditorPanel extends javax.swing.JPanel {
    private MyDatabase mysql;
    private String mtable;
    private String catalog;
 

    /** Creates new form DBEditorPanel */
    public DBEditorPanel() {
        initComponents();
        enableComponents(false);
        
        JLabel jLabelTruncar = new JLabel("Truncar");
        JLabel jLabelEliminar = new JLabel("Eliminar");
        menuButton1.setItems(new JLabel[]{jLabelTruncar, jLabelEliminar});
        menuButton1.registerActionListener(0, new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    doTruncate();
                }
            });
        menuButton1.registerActionListener(1, new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    doDrop();
                }
            });
        
         
        jTable1.setIntercellSpacing( new java.awt.Dimension(2,2) );
        jTable1.setGridColor(java.awt.Color.gray);
        jTable1.setShowGrid(true);
        
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        
    }
         
    public void reset()
    {
         enableComponents(false);
         jTable1.setModel(new DefaultTableModel());
    }

    public final void enableComponents(boolean enable)
    {
        jToolBar1.setEnabled(enable);
        jButton2.setEnabled(enable);
        jButton3.setEnabled(enable);
        jButton4.setEnabled(enable);
        jButton5.setEnabled(enable);
        jButton6.setEnabled(enable);
        jButton7.setEnabled(enable);
        menuButton1.setEnabled(enable);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton6 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        menuButton1 = new org.iesapp.apps.mysqlbrowser.widgets.menubutton.MenuButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new org.iesapp.apps.mysqlbrowser.table.JDbTable();

        jPanel1.setName("jPanel1"); // NOI18N

        jToolBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jToolBar1.setName("jToolBar1"); // NOI18N

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/insert.gif"))); // NOI18N
        jButton2.setToolTipText("Inserta una fila");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jSeparator7.setName("jSeparator7"); // NOI18N
        jToolBar1.add(jSeparator7);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/delete.gif"))); // NOI18N
        jButton3.setToolTipText("Esborra files");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar1.add(jSeparator5);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/excel_importt.gif"))); // NOI18N
        jButton4.setToolTipText("Importa xls");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/excel_export.gif"))); // NOI18N
        jButton7.setToolTipText("Exporta xls");
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setName("jButton7"); // NOI18N
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/save.gif"))); // NOI18N
        jButton5.setToolTipText("Desa a la base de dades");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/refresh.gif"))); // NOI18N
        jButton6.setToolTipText("Refresh");
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setName("jButton6"); // NOI18N
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        menuButton1.setText("Més operacions");
        menuButton1.setFocusable(false);
        menuButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menuButton1.setName("menuButton1"); // NOI18N
        menuButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(menuButton1);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        fillTable();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       jTable1.commitChangesToDB();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        FileDialog fd = new FileDialog((JFrame) null, "Exporta Excel...", FileDialog.SAVE);
        fd.setFile(mtable + ".xls");
        fd.setLocationRelativeTo(null);
        fd.setVisible(true);

        String file = fd.getFile();
        String dir = fd.getDirectory();
        if (file == null || dir == null) {
            return;
        }
        if (!StringUtils.AfterLast(file, ".").trim().equals("xls")) {
            file += ".xls";
        }

        try {


            WritableWorkbook workbook = Workbook.createWorkbook(new File(dir + file));
            WritableSheet sheet = workbook.createSheet("Pagina 1", 0);

            //headers
            for (int i = 0; i < jTable1.getColumnCount(); i++) {
                sheet.addCell(new jxl.write.Label(i, 0, ((JLabel) jTable1.getColumnModel().getColumn(i).getHeaderValue()).getText()));
            }

            jxl.write.DateFormat customDateFormat = new jxl.write.DateFormat("dd/mm/yyyy");
            for (int i = 0; i < jTable1.getRowCount(); i++) {


                for (int j = 0; j < jTable1.getColumnCount(); j++) {

                    Object obj = jTable1.getValueAt(i, j);
                    if(obj!=null)
                    {
                    if (obj.getClass().equals(Integer.class)) {
                        int val = ((Number) obj).intValue();
                        sheet.addCell(new jxl.write.Number(j, i + 1, val));
                        
                    } else if (obj.getClass().equals(String.class) || obj.getClass().equals(LongText.class)) {
                        sheet.addCell(new jxl.write.Label(j, i + 1, (String) obj));
                    } else if (obj.getClass().equals(java.sql.Date.class)) {
                        

                        jxl.write.WritableCellFormat dateFormat =
                                new jxl.write.WritableCellFormat(customDateFormat);

                        java.sql.Date datasql = (java.sql.Date) obj;
                        java.util.Date data = new java.util.Date(datasql.getTime());

                        sheet.addCell(new jxl.write.DateTime(j, i + 1, data, dateFormat));
                    }
                    else
                    {
                        sheet.addCell(new jxl.write.Label(j, i + 1, obj.toString()));
                    }
                    }
                    else
                    {
                        sheet.addCell(new jxl.write.Label(j, i + 1, "(NULL)"));
                    }

                }
            }


            for (int i = 0; i < jTable1.getColumnCount(); i++) {
                jxl.CellView cell = sheet.getColumnView(i);
                cell.setAutosize(true);
                sheet.setColumnView(i, cell);
            }

            workbook.write();
            workbook.close();

        } catch (IOException ex) {
            Logger.getLogger(DBEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (jxl.write.WriteException ex) {
            Logger.getLogger(DBEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        }


        try {
            java.awt.Desktop.getDesktop().open(new File(dir + file));
        } catch (IOException ex) {
            // MyLogger.log("No es pot obrir "+ cfg.doc , MyLogger.logWARNING);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

  
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        jTable1.deleteRows();
      
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTable1.addRow();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        ExcelImportWiz wizard = new ExcelImportWiz(mysql, "`"+catalog+"`."+mtable);
        if (wizard.showModalDialog() == 0) {
            fillTable();
        }

    }//GEN-LAST:event_jButton4ActionPerformed

//  
//        
//        //Crea un row Sorterer
//        if(mfields.length>0)
//        {
////            String[] reversed = new String[mfields.length];
////            for(int i=0; i<mfields.length; i++)
////            {
////                reversed[i] = mfields[mfields.length-i-1];
////            }
//            mySorter1 = new MySorter(mfields);
//            
//            JTableHeader header = jTable1.getTableHeader();
//            header.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                        JTable table = ((JTableHeader) evt.getSource()).getTable();
//                        TableColumnModel colModel = table.getColumnModel();
//
//                        int index = colModel.getColumnIndexAtX(evt.getX());
//                        if(index>=0 && index<mfields.length)
//                        {
//                            mySorter1.setFirst(mfields[index]);
//                            fillTable();
//                        }
//                        
//                        java.awt.Rectangle headerRect = table.getTableHeader().getHeaderRect(index);
//                        if (index == 0) {
//                          headerRect.width -= 10;
//                        } else {
//                          headerRect.grow(-10, 0);
//                        }
//                        if (!headerRect.contains(evt.getX(), evt.getY())) {
//                          int vLeftColIndex = index;
//                          if (evt.getX() < headerRect.x) {
//                            vLeftColIndex--;
//                          }
//                        }
//            }
//         });
//            
//            
//            
//        }
////       for(int i=0; i<nFields; i++) System.out.println(mfields[i]);
////       for(int i=0; i<nFields; i++) System.out.println(medit[i]);
////       for(int i=0; i<nFields; i++) System.out.println(mtypes[i]);
////       System.out.println("key="+pKey);
////       
//
//    }
//
//    private void InsertRow() {
//        String SQL1 = "Select max(" + pKey + ") as idmax FROM " + mtable;
//        ResultSet rs1 = null;
//        int id = 0;
//        try {
//
//            rs1 = mysql.getResultSet(SQL1);
//            if (rs1 != null && rs1.next()) {
//                id = rs1.getInt("idmax");
//            }
//
//        } catch (SQLException ex) {
//            Logger.getLogger(DBEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        id += 1;
//
//
//        //update Database
//        String myfields = "";
//        String myvalues = "";
//
//        if (nFields == 0) {
//            return;
//        }
//
//        Object[] obj = new Object[nFields];
//
//        if (nFields >= 1) {
//            myfields = mfields[0];
//            myvalues += "?";
//            obj[0] = id;
//        }
//
//        for (int i = 1; i < nFields; i++) {
//            myfields += " ," + mfields[i];
//            myvalues += " ,?";
//
//            if (mtypes[i].getClass().equals(LongText.class)) {
//                LongText lt = (LongText) mtypes[i];
//                obj[i] = lt.getText();
//            } else {
//                obj[i] = mtypes[i];
//            }
//        }
//
//
//        SQL1 = "INSERT INTO " + mtable + "  (" + myfields + ")  VALUES(" + myvalues + ")";
//        int nup = mysql.preparedUpdate(SQL1, obj);
////        System.out.println(SQL1+"; "+nup);
//
//        if (nup > 0) {
//            modelTable1.addRow(obj);
//        }
//
//    }
//
//    /**
//     * make sure a given rowIndex is visible
//     *
//     * @param rowIndex 0-based rowIndex to make sure is scrolled
//    into view.
//     */

//
//    private void refresh() {
//        isListening = false;
//        fillTable();
//        isListening = true;
//        canvis.clear();
//        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/save.gif")));
//    }
//
//    private void CommitToDB() {
//        //surt de mode edicio
//        if (jTable1.isEditing()) {
//            jTable1.editCellAt(0, 0);
//        }
//
//        if (canvis.isEmpty()) {
//            System.out.println("Empty canvis, returning");
//            return;
//        }
//
//        Iterator<Integer> iter = canvis.iterator();
//        while (iter.hasNext()) {
//            int row = iter.next().intValue();
//
//            Object obj = jTable1.getValueAt(row, pospKey);
//
//            String id = obj.toString();
//
//            //update Database
//            String myfields = "";
//
//            if (nFields == 0) {
//                return;
//            }
//            if (nFields >= 1) {
//                myfields = mfields[0] + "=? ";
//            }
//
//            for (int i = 1; i < nFields; i++) {
//                
//                if(!mtypes[i].getClass().equals(javax.swing.JLabel.class))
//                    myfields += " ," + mfields[i] + "=? ";
//            }
//
//            String SQL1 = "UPDATE " + mtable + " SET " + myfields
//                    + " WHERE " + pKey + "='" + id + "'";
//
//            Object[] updatedValues = new Object[nFields];
//
//            for (int i = 0; i < nFields; i++) {
//                updatedValues[i] = jTable1.getValueAt(row, i);
//            }
//
//            //HOW TO DEAL WITH NULL TYPE?
//            int nup = mysql.preparedUpdate(SQL1, updatedValues);
//            System.out.println("nup="+nup+"; SQL="+SQL1);
//        }
//
//
//
//        canvis.clear();
//        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/save.gif")));
//    }
//    
//     private void createTableModel()
//     {
//     //nFields and mtypes should be automatically generated    
//         
//        modelTable1 = new DefaultTableModel(
//                new Object [][] {
//            },
//            mfields);
//        jTable1.setModel(modelTable1);
//        
//        //addListener
//         jTable1.getModel().addTableModelListener( new TableModelListener()
//              {
//              public void tableChanged(TableModelEvent e)
//               {
//                    int mrow = e.getFirstRow();
//                    int mcol = e.getColumn();
//                    if(isListening)
//                    {
//                        canvis.add(mrow);
//
//                    if(canvis.isEmpty())
//                         jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/save.gif")));
//                    else
//                         jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/save2.gif")));
//
//
//                    }
//              }
//        });
//
//        //set Renderers and Editors
//        for(int i=0; i<nFields; i++)
//        {
//            int calculated = mfields[i].length()*10;
//            jTable1.getColumnModel().getColumn(i).setPreferredWidth(calculated>100?calculated:100);
////            if(mtypes[i].getClass().equals(java.sql.Date.class))
////            {
////                jTable1.getColumnModel().getColumn(i).setCellEditor(new CellDateEditor());
////                jTable1.getColumnModel().getColumn(i).setCellRenderer(new CellDateRenderer());
////            }
//            if(mtypes[i].getClass().equals(LongText.class))
//            {
//                jTable1.getColumnModel().getColumn(i).setCellEditor(new TextAreaEditor());
//                //jTable1.getColumnModel().getColumn(i).setCellRenderer(new TextAreaRenderer());
//            }
//            else if(mtypes[i].getClass().equals(javax.swing.JLabel.class))
//            {
//                jTable1.getColumnModel().getColumn(i).setCellRenderer(new MyIconLabelRenderer());
//            }
//        }
// }
//
    private void fillTable() {

        //erase table
        //jTable1.clear();
        jTable1.setTable( catalog, mtable, mysql );
//        try {
//            
//            while (rs1 != null && rs1.next()) {
//                Object[] linia = new Object[nFields];
//                for (int i = 0; i < nFields; i++) {
//                   
//                    if (mtypes[i].getClass().equals(Integer.class)) {
//                        linia[i] = rs1.getInt(mfields[i]);
//                       
//                    } else if (mtypes[i].getClass().equals(String.class) || mtypes[i].getClass().equals(LongText.class)) {
//                        linia[i] = rs1.getString(mfields[i]);
//                    }
//                    else if (mtypes[i].getClass().equals(java.sql.Date.class)) {                      
//                        linia[i] = rs1.getDate(mfields[i]);
//                    }
//                    else if (mtypes[i].getClass().equals(java.lang.Double.class)) {
//                        linia[i] = rs1.getDouble(mfields[i]);
//                    }
//                    else if (mtypes[i].getClass().equals(java.lang.Float.class)) {
//                        linia[i] = rs1.getFloat(mfields[i]);
//                    }
//                    else if (mtypes[i].getClass().equals(java.lang.Boolean.class)) {
//                        linia[i] = rs1.getBoolean(mfields[i]);
//                    }
//                    else if (mtypes[i].getClass().equals(java.sql.Time.class)) {
//                        linia[i] = rs1.getTime(mfields[i]).toString();
//                    }
//                    else if (mtypes[i].getClass().equals(javax.swing.JLabel.class)) {
//                        byte[] bytes = rs1.getBytes(mfields[i]);                        
//                        linia[i] = getImageLabel(bytes);
//                    }
//                    else
//                    {
//                        throw(new java.lang.UnsupportedOperationException(mtypes[i] + " not supported"));
//                    }
//                }
//                modelTable1.addRow(linia);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DBEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }


     public void setTable(MyDatabase mysql, String dbName, String tableName) {  
        this.mysql = mysql;
        //This is very dangerous (use instead catalog.table)
        //this.mysql.setCatalog(dbName);
        mtable = tableName;
        catalog = dbName;
        fillTable();
        if(jTable1.isHasPK())   {
            enableComponents(true);
        }
     }
//        
//        pKey = null;
//        //hard reset
//        canvis = new ArrayList<Integer>();
//        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/save.gif")));
//                   
//        getColumnInfo();
//        
//        //Aquesta taula no conté index primari i l'edicio no esta suportada
//        if(pKey==null)
//        {
//            for(int i=0; i<medit.length; i++)
//                medit[i] = false;
//            
//            enableComponents(false);
//        }
//        else
//        {
//             enableComponents(true);
//        }
//        
//        createTableModel();
//        fillTable();
//       
//        isListening = true;
//        
//    }
     
     
    private javax.swing.JLabel getImageLabel(byte[] foto)
    {
        JLabel label = new JLabel();
        if(foto!=null)
        {
            int heigth2 = jTable1.getRowHeight();
            java.awt.Image image = Toolkit.getDefaultToolkit().createImage(foto);
            java.awt.Image scaledInstance = image.getScaledInstance(-1, heigth2, java.awt.Image.SCALE_SMOOTH);
            label.setIcon( new javax.swing.ImageIcon(scaledInstance) );
        }
        return label;
    }
   
    private void doTruncate() {
             
         Object[] options = {"No", "Si"};
        String missatge = "Voleu truncar la taula?\nEs buidaran totes les dades.";

        int n = JOptionPane.showOptionDialog(this,
                missatge, "Confirma truncar",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (n != 1) {
            return;
        }
        mysql.truncate(mtable);
        jTable1.clear();
   }  
    
    private void doDrop() {
             
        Object[] options = {"No", "Si"};
        String missatge = "Voleu eliminar la taula?\nEs perdran totes les dades.";

        int n = JOptionPane.showOptionDialog(this,
                missatge, "Confirma eliminar",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (n != 1) {
            return;
        }
        mysql.executeUpdate("DROP TABLE IF EXISTS `"+mtable+"`");
        this.reset();   
   }    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator7;
    private org.iesapp.apps.mysqlbrowser.table.JDbTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private org.iesapp.apps.mysqlbrowser.widgets.menubutton.MenuButton menuButton1;
    // End of variables declaration//GEN-END:variables

    public void enableMoreOperations(boolean enable) {
        menuButton1.setEnabled(enable);
    }

    public void enableAddRow(boolean enable)
    {
        jButton2.setEnabled(enable);
    }
 
    public void enableDeleteRow(boolean enable)
    {
        jButton3.setEnabled(enable);
    }
 
    public void enableImportXls(boolean enable)
    {
        jButton4.setEnabled(enable);
    }
 
    public void enableExportXls(boolean enable)
    {
        jButton7.setEnabled(enable);
    }
    
    public void setEditability(boolean[] editable)
    {
        jTable1.setEditable(editable);
    }


   
}
