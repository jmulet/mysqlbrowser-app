/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.mysqlbrowser.wizard;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import jxl.Cell;
import jxl.Sheet;
import org.iesapp.database.FieldDescriptor;
import org.iesapp.util.StringUtils;

/**
 *
 * @author Josep
 */
public class Step3 extends javax.swing.JPanel {
    private DefaultListModel model1, model2;
    private DefaultComboBoxModel combomodel1;
    private DefaultTableModel modelTable1;
    private Sheet sheet;
    private HashMap<String, Integer> asoc;
    private ArrayList<FieldDescriptor> descriptorForTable;
   

    /**
     * Creates new form StartupStep3
     */
    public Step3() {
        initComponents();
        jTable1.setIntercellSpacing( new java.awt.Dimension(2,2) );
        jTable1.setGridColor(java.awt.Color.gray);
        jTable1.setShowGrid(true);
        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);
        descriptorForTable = ExcelImportWiz.mysql.getDescriptorForTable(ExcelImportWiz.tableName);
    }
    
    public void onShow(Sheet sheet, HashMap<String,Integer> asoc)
    {
        this.sheet = sheet;
        this.asoc = asoc;
        
        
        generatePreview();
    }
    
     private void generatePreview() {
    
         if(asoc==null) {
            return;
        }
         
       
         int ncolstaula = descriptorForTable.size();
         String[] fields = new String[ncolstaula];
         for (int i = 0; i < ncolstaula; i++) {
             fields[i] = descriptorForTable.get(i).getName();
         }
         modelTable1 = new javax.swing.table.DefaultTableModel(new Object[][]{}, fields);
         jTable1.setModel(modelTable1);

         //Append
         int lastId = 1;
         if(jRadioButton2.isSelected())
         {
             String SQL1 = "SELECT * FROM "+ ExcelImportWiz.tableName;
            
                          
             try {
             Statement st = ExcelImportWiz.mysql.createStatement();
             ResultSet rs1 = ExcelImportWiz.mysql.getResultSet(SQL1,st); 
                
                while(rs1!=null && rs1.next())
                {
                     Object[] obj = new Object[ncolstaula];
                     for(int i=1; i<ncolstaula+1; i++)
                     {
                       obj[i-1] = rs1.getObject(i);
                     }
                     modelTable1.addRow(obj);
                     lastId +=1;
                }
                if(rs1!=null) {
                    rs1.close();
                    st.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Step3.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
       
         String txt = jTextField2.getText().trim();
         ArrayList<String> parsedArray = new ArrayList<String>();
         if(!txt.isEmpty()) {
            parsedArray = StringUtils.parseStringToArray(txt, " ", StringUtils.CASE_INSENSITIVE);
        }
         
         //Afegir les dades del fitxer EXCEL
         int nrows = sheet.getRows();
         int ncols = sheet.getColumns();
         int nFields = descriptorForTable.size();
         int iniRow = ((Integer) jSpinner1.getValue())-1;
         if(iniRow<0) {
            iniRow = 0;
        }
         for(int i = iniRow; i<nrows; i++)
         {
             Object[] obj = new Object[nFields];
             for(int j=0; j<nFields; j++)
             {
                 FieldDescriptor get = descriptorForTable.get(j);
                 String key = get.getName();
                
                 int colExcel = asoc.get(key);
                 if(colExcel>=0 && colExcel<ncols)
                 {
                     Cell cell = sheet.getCell(colExcel, i);
                     if(cell.getType()== jxl.CellType.LABEL)
                     {
                        String str = cell.getContents();
                        for(int k=0; k<parsedArray.size(); k++)
                        {
                             str = str.replaceAll(parsedArray.get(k), "");
                        }
                        if(jCheckBox1.isSelected()) {
                            str  = str.trim();
                        }
                        obj[j] = str;
                     }
                     else if(cell.getType()== jxl.CellType.DATE)                     
                     {
                         jxl.DateCell dc = (jxl.DateCell) cell;
                         obj[j] = new java.sql.Date(dc.getDate().getTime());
                     }
                     else if(cell.getType()== jxl.CellType.NUMBER)                     
                     {
                         jxl.NumberCell nc = (jxl.NumberCell) cell;
                         if(get.getType().contains("int") || get.getType().contains("tinyint")) {
                            obj[j] = (int) nc.getValue();
                        }
                         else {
                            obj[j] = nc.getValue();
                        }
                     }
                     else if(cell.getType()== jxl.CellType.EMPTY)                     
                     {
                         obj[j] = "";
                     }
                     
                 }
                 else if(colExcel==-1)
                 {
                     obj[j] = lastId;
                 }        
                 else if(colExcel==-2)
                 {
                     obj[j] = "";
                 }                
             }
              lastId +=1;
             modelTable1.addRow(obj);
         }
             
         
     }

    //Commit changes to database
    public void doSave() {
    
         if(jRadioButton1.isSelected()) {
            ExcelImportWiz.mysql.truncate( ExcelImportWiz.tableName );
        }
        
         String txt = jTextField2.getText().trim();
         ArrayList<String> parsedArray = new ArrayList<String>();
         if(!txt.isEmpty()) {
            parsedArray = StringUtils.parseStringToArray(txt, " ", StringUtils.CASE_INSENSITIVE);
        }
       
         //fields
         String fields = "(";
         String values = "(";
         int nfieldsReal = 0;
         for(int i=0; i<descriptorForTable.size(); i++)
         {
           FieldDescriptor desc = descriptorForTable.get(i);
           if(!desc.isAutoIncrement())
           {
                fields += desc.getName()+",";
                values += "?,";
                nfieldsReal +=1;
           }
         }
         fields = StringUtils.BeforeLast(fields, ",") + ")";       
         values = StringUtils.BeforeLast(values, ",") + ")";       
         String query = "INSERT INTO `"+ExcelImportWiz.tableName+"` "+fields+" VALUES"+values;
         
         //Afegir les dades del fitxer EXCEL
         int nrows = sheet.getRows();
         int ncols = sheet.getColumns();
         int nFields = descriptorForTable.size();
         int iniRow = ((Integer) jSpinner1.getValue())-1;
         if(iniRow<0) {
            iniRow = 0;
        }
         for(int i = iniRow; i<nrows; i++)
         {
             Object[] obj = new Object[nfieldsReal];
             int jpos = 0;
             for(int j=0; j<nFields; j++)
             {
                 FieldDescriptor get = descriptorForTable.get(j);
                 String key = get.getName();
                
                 int colExcel = asoc.get(key);
                 if(colExcel>=0 && colExcel<ncols)
                 {
                     Cell cell = sheet.getCell(colExcel, i);
                     if(cell.getType()== jxl.CellType.LABEL)
                     {
                        String str = cell.getContents();
                        for(int k=0; k<parsedArray.size(); k++)
                        {
                             str = str.replaceAll(parsedArray.get(k), "");
                        }
                        if(jCheckBox1.isSelected()) {
                            str  = str.trim();
                        }
                        obj[jpos] = str;
                        jpos += 1;
                     }
                     else if(cell.getType()== jxl.CellType.DATE)                     
                     {
                         jxl.DateCell dc = (jxl.DateCell) cell;
                         obj[jpos] = new java.sql.Date(dc.getDate().getTime());
                         jpos += 1;
                     }
                     else if(cell.getType()== jxl.CellType.NUMBER)                     
                     {
                         jxl.NumberCell nc = (jxl.NumberCell) cell;
                         
                         if(get.getType().equalsIgnoreCase("int") || get.getType().equalsIgnoreCase("tinyint")) {
                            obj[jpos] = (int) nc.getValue();
                        }
                         else {
                            obj[jpos] = nc.getValue();
                        }
                         
                         jpos += 1;
                     }
                     else if(cell.getType()== jxl.CellType.EMPTY)                     
                     {
                         obj[jpos] = "";
                         jpos += 1;
                     }
                     
                 }
                 else if(colExcel==-1) //PrimaryKey (autoincrement)
                 {
                     //
                 }        
                 else if(colExcel==-2)
                 {
                     obj[jpos] = "";
                     jpos += 1;
                 }                
             }
              
             //System.out.println(query);
             for(Object oo: obj) {
                //System.out.println(oo);
            }
             int nup = ExcelImportWiz.mysql.preparedUpdate(query, obj);
             
         }
         
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }}
            ;

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel1.setText("Opcions d'importació");

            jRadioButton1.setSelected(true);
            jRadioButton1.setText("Reemplaça-les");
            jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jRadioButton1ActionPerformed(evt);
                }
            });

            jLabel6.setText("Com importar les dades?");

            jLabel5.setText("(separats amb espai)");

            jCheckBox1.setSelected(true);
            jCheckBox1.setText("Elimina espais al principi i al final de les paraules");
            jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox1ActionPerformed(evt);
                }
            });

            jLabel3.setText("Començar a importar des de la fila");

            jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    jTextField2KeyReleased(evt);
                }
            });

            jLabel4.setText("Eliminar els següents caràcters");

            jSpinner1.setValue(1);
            jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    jSpinner1StateChanged(evt);
                }
            });

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jCheckBox1)
                    .addGap(255, 255, 255))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField2)
                        .addComponent(jSpinner1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel5)
                    .addGap(90, 90, 90))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addGap(0, 18, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jCheckBox1))
            );

            jRadioButton2.setText("Afegint-les");
            jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jRadioButton2ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jRadioButton1)
                        .addComponent(jLabel6)
                        .addComponent(jRadioButton2))
                    .addGap(29, 29, 29))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jRadioButton1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jRadioButton2)
                    .addContainerGap())
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 9, Short.MAX_VALUE))
            );

            jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel2.setText("Previsualització de la taula després de la importació");

            jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            modelTable1 = new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {}
            );
            jTable1.setModel(modelTable1);
            jTable1.setRowHeight(40);
            jScrollPane2.setViewportView(jTable1);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                    .addGap(0, 0, Short.MAX_VALUE)))))
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                    .addContainerGap())
            );
        }// </editor-fold>//GEN-END:initComponents

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        generatePreview();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
       generatePreview();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        generatePreview();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        generatePreview();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
       generatePreview();
    }//GEN-LAST:event_jSpinner1StateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

   
   
   
}
