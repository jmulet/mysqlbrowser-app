/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.mysqlbrowser.table;

import java.awt.Component;
import java.awt.Rectangle;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.iesapp.database.FieldDescriptor;
import org.iesapp.database.MyDatabase;
import org.iesapp.util.StringUtils;

/**
 * Represents a database table which can represent
 * resultsets
 * @author Josep
 */
public class JDbTable extends javax.swing.JTable{
    private String basicQuery;
    private TableColumnAdjuster tca;
    private String catalog;

    public boolean[] getEditable() {
        return editable;
    }

    public void setEditable(boolean[] editable) {
        this.editable = editable;
    }

    private static class JComponentTableCellRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return (JComponent) value;
        }
    }

    //Struct to hold primary keys
    private static class PKStruct {
        public int tableCol;
        public boolean isAutoIncrement;
        
        public PKStruct(int tableCol, boolean isAutoIncrement) {
            this.tableCol = tableCol;
            this.isAutoIncrement = isAutoIncrement;
        }
    }

    private static class PreparedUpdate {

        public String query;
        public Object[] row;
        public PreparedUpdate(String query, Object[] obj) {
            this.query = query;
            this.row = obj;
        }
    }
    
    protected ResultSet resultSet;
    private DefaultTableModel model;
    protected boolean[] editable;
    private ResultSetMetaData metaData;
    private ArrayList<FieldDescriptor> descriptors;
    private MyDatabase mysql;
    private String tableName;
    private boolean hasPK = false;
    private String pkName;
    private int pkColumn;
    private int ncols;
    private boolean isListening = false;
    private HashMap<Object,PreparedUpdate> changes;
    
    
    //Improved Primary keys management
    private HashMap<String,PKStruct> mapPKs;
    private int totalAutoInc = 0;
    
    public JDbTable()
    {
        
         model = new DefaultTableModel();
       
        //some style issues (by default, which can be overwritten)
         this.setIntercellSpacing( new java.awt.Dimension(2,2) );
         this.setGridColor(java.awt.Color.gray);
         this.setShowGrid(true);
         this.setRowHeight(40);
         this.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
         this.getTableHeader().setReorderingAllowed(false);
         this.setAutoCreateRowSorter(true);
        
    }
    
  

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setTable(String catalog, String table, MyDatabase mysql) {
        isListening = false;
        changes = new HashMap<Object,PreparedUpdate>();
        mapPKs = new HashMap<String,PKStruct>();
        totalAutoInc = 0;
        
        try {
            this.mysql = mysql;
            this.catalog = catalog;
            this.tableName = table;
            this.descriptors = mysql.getDescriptorForTable("`"+catalog+"`."+table);
            Statement st = mysql.createStatement();
            this.resultSet = mysql.getResultSet("SELECT * FROM `"+catalog+"`."+table, st);
         
            
            if(resultSet==null) {
                return;
            }
                //Create a model
                metaData = resultSet.getMetaData();
                ncols = metaData.getColumnCount();
                String[] fields = new String[ncols];
                editable = new boolean[ncols];
               
                for(int i=1; i<ncols+1; i++)
                {
                    String name = metaData.getColumnName(i);
                    fields[i-1] = name;
                    boolean auto = metaData.isAutoIncrement(i);
                    editable[i-1] = !auto;  //Es editable sempre que no sigui autoincrement               
                    if(auto){
                        totalAutoInc += 1;
                    }
                    
                    //Mapa de pks
                    if(descriptors.get(i-1).isPrimaryKey())
                    {
                        mapPKs.put(name, new PKStruct(i-1,auto));
                    }
                }
                
                //System.out.println("this table has #autoincrement = "+totalAutoInc);
                
//               //Detect FIRST PK WHICH IS AUTOINCREMENT
//               hasPK = false; 
//               for (int i = 0; i < ncols ; i++) {
//                if (descriptors.get(i).isPrimaryKey() && descriptors.get(i).isAutoIncrement()) {
//                        hasPK = true;
//                        pkName = descriptors.get(i).getName();
//                        pkColumn = i;
//                        break;
//                }
//               }
//               
//               //Detect FIRST PK WHICH IS NOT AUTOINCREMENT
//               if(!hasPK)
//               {
//                   for (int i = 0; i < ncols ; i++) {
//                    if (descriptors.get(i ).isPrimaryKey()) {
//                        hasPK = true;
//                        pkName = descriptors.get(i).getName();
//                        pkColumn = i;
//                        break;
//                    }
//               }
//               }
                
                if(mapPKs.isEmpty()) //table without Primary Key will be read-only
                {
                    for(int i=0; i<ncols; i++) {
                        getEditable()[i]=false;
                    }
                }
                
                model = new javax.swing.table.DefaultTableModel(new Object[][]{}, fields){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return editable[column];
                    }
                };
                
           
                
                model.addTableModelListener(new TableModelListener()
                {

                    @Override
                    public void tableChanged(TableModelEvent e) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        TableModel model2 = (TableModel) e.getSource();
                        // String columnName = model2.getColumnName(column);
                        
                        // Register changes
                        if (isListening && changes != null) {
                            //System.out.println("event at row/col "+row+","+column+" - "+e.getType());
                                                                                    
                            int ncols2 = model2.getColumnCount();                            
                            Object[] obj = new Object[ncols2-totalAutoInc]; //Les autoIncrementables no es fa l'update
                            int pos = 0;
                            String mapidentifier = "";
                            String updatecondition = "";
                            for (int i = 0; i < ncols2; i++) {
                            //
                                FieldDescriptor fd = descriptors.get(i);
                                if(fd.isPrimaryKey())
                                {
                                    Object val = model2.getValueAt(row,i);
                                    mapidentifier += val + ";";
                                    if(!updatecondition.isEmpty()){
                                        updatecondition +=" AND ";
                                    }
                                    updatecondition += fd.getName()+"='"+ val + "' ";
                                }
                                
                                if(!fd.isAutoIncrement())
                                {
                                    //System.out.println("accedim a pos="+pos+ " quan la mida de obj es "+obj.length+"  i="+i  );
                                    obj[pos] = model2.getValueAt(row, i);
                                    pos +=1;
                                }
                                
                           
                                
                            }
                            String query = basicQuery+" "+updatecondition;
                            changes.put(mapidentifier, new PreparedUpdate(query,obj));
                        }
        
    }
                    
                });
                
                //Determine convinient column cell renderer and editor
                
                
                this.setModel(model);
                
                RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
                this.setRowSorter(sorter);
                tca = new TableColumnAdjuster(this);
                tca.setColumnHeaderIncluded(true);
                tca.setColumnDataIncluded(true);
                tca.setMaximumSize(300);
              
                
                //Redefine headers
                   
                TableCellRenderer renderer = new JComponentTableCellRenderer();
                Border headerBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
                for(int i=1; i<ncols+1; i++)
                {
                    FieldDescriptor fd = descriptors.get(i-1);
                    String name = metaData.getColumnName(i);
                  
                    this.getColumnModel().getColumn(i-1).setHeaderRenderer(renderer);
                    JLabel jlabel = new JLabel(name);
                    if(fd.isAutoIncrement() && fd.isPrimaryKey()) {
                        jlabel.setIcon(new ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/keyauto.png")));
                    }
                    else if(!fd.isAutoIncrement() && fd.isPrimaryKey()) {
                        jlabel.setIcon(new ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/key.png")));
                    }
                    else if(fd.isAutoIncrement() && !fd.isPrimaryKey()) {
                        jlabel.setIcon(new ImageIcon(getClass().getResource("/org/iesapp/apps/mysqlbrowser/icons/auto.png")));
                    }
                    
                    jlabel.setBorder(headerBorder);
                    this.getColumnModel().getColumn(i-1).setHeaderValue(jlabel);
                 
                   
                }
                
                addResultSetToTable(resultSet, st);
        } catch (SQLException ex) {
            Logger.getLogger(JDbTable.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        
          
        String fields = "";
        
        for(int i=0; i<ncols; i++)
        {
            if(!descriptors.get(i).isAutoIncrement())
            {
                fields += descriptors.get(i).getName()+"=?,";
               
            }
        }
        fields = StringUtils.BeforeLast(fields, ",");
        
        basicQuery = "UPDATE `"+catalog+"`."+tableName+" SET "+fields+" WHERE ";
        
        tca.adjustColumns();
        isListening = true;    
    }

    public void clear() {
        isListening = false;
        while(this.getRowCount()>0){
             model.removeRow(0);
        }
            
        isListening = true;
        changes = new HashMap<Object,PreparedUpdate>();
    }

    /**
     * Add a new blank row to the table
     */
    public void addRow() {
        if(!isHasPK()) {
            return;
        }
        
        isListening = false;
        String fields="";
        String values="";
        int nextid=0;
        String keyCondition="";
        
        //Analitza tots els descriptors, si son claus primàries sense autoincrement els assigna un valor
        for(FieldDescriptor fd: descriptors)
        {
            if( fd.isPrimaryKey() && !fd.isAutoIncrement()) //Es una clau primaria pero no te autoincrement
            {
                String SQL1 = "SELECT MAX("+fd.getName()+") FROM `"+catalog+"`."+tableName;
                
                
                try {
                    Statement st = mysql.createStatement();
                    ResultSet rs = mysql.getResultSet2(SQL1,st);
                    if(rs!=null && rs.next())
                    {
                        nextid = rs.getInt(1) + 1;
                    } 
                    if(rs!=null){
                        rs.close();
                        st.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JDbTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(nextid>0)
                {
                     fields += fd.getName()+",";
                     values += nextid+",";
                     if(!keyCondition.isEmpty()) {
                        keyCondition +=" AND ";
                    }
                     keyCondition += fd.getName()+"="+nextid;
                }                  
            }
            else if( fd.isPrimaryKey() && fd.isAutoIncrement())
            {
                     if(!keyCondition.isEmpty()) {
                    keyCondition +=" AND ";
                }
                     keyCondition += fd.getName()+"=?";
            }
            else if(!fd.isPrimaryKey() && !fd.isAutoIncrement() && !fd.isNulo() && StringUtils.noNull(fd.getDefecte()).isEmpty())
            {
                fields += fd.getName()+",";
                if(fd.getType().startsWith("int") || fd.getType().startsWith("tinyint") ) {
                    values += "0,";
                }
                else if(fd.getType().startsWith("float") || fd.getType().startsWith("double") ) {
                    values += "0.0,";
                }
                else if(fd.getType().startsWith("char") || fd.getType().startsWith("varchar") || fd.getType().startsWith("longtext") ) {
                    values += "'',";
                }
                else if(fd.getType().startsWith("date") ) {
                    values += "'0000-00-00',";
                }
                else if(fd.getType().startsWith("time") ) {
                    values += "'00:00:00',";
                }
                else if(fd.getType().startsWith("timestamp") ) {
                    values += "'0000-00-00 00:00:00',";
                }
                else {
                    values +="'',";
                }
            }
        }
        if(!fields.isEmpty())
        {
            fields = StringUtils.BeforeLast(fields, ",");
            values = StringUtils.BeforeLast(values, ",");
        }
        String SQL1 = "INSERT INTO `"+catalog+"`."+tableName+" ("+fields+") VALUES("+values+")";     
        Object[] keysObj = mysql.executeUpdateKEYS(SQL1);
      
        //llegeix aquesta fila i l'afegeix a la taula
        String SQL2 = "SELECT * FROM `"+catalog+"`."+tableName+" WHERE "+keyCondition;     
        
        //System.out.println("afegim el resultat de "+SQL2);
        
        ResultSet rs2;
        Statement st2 =null;
        try {
            st2 = mysql.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(JDbTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(keysObj==null) {
            rs2= mysql.getResultSet(SQL2,st2);
        }
        else {
            rs2= mysql.getPreparedResultSet(SQL2, keysObj);
        }
        
        addResultSetToTable(rs2, st2);
        
        int n = this.getRowCount()-1;
        ensureVisible(n);
        
        isListening = true;
    }
    
    private void ensureVisible(int rowIndex) {
        // corral into bounds
        rowIndex = Math.max(0, Math.min(rowIndex, this.getRowCount() - 1));
        final Rectangle r = this.getCellRect(rowIndex, 0/* col
                 */, true);
        this.scrollRectToVisible(r);
    }
    
    
    private void addResultSetToTable(final ResultSet rs, final Statement st)
    {
        try {
            while(rs.next())
                {
                   Object[] arow = new Object[ncols];
                   for(int i=1; i<ncols+1; i++)
                   {
                       if(metaData.getColumnType(i)==java.sql.Types.INTEGER ||
                          metaData.getColumnTypeName(i).equals("TINYINT") )
                       {
                            arow[i-1] =  rs.getInt(i);
                       }
                       else if(metaData.getColumnType(i)==java.sql.Types.VARCHAR ||
                               metaData.getColumnType(i)==java.sql.Types.LONGVARCHAR ||
                               metaData.getColumnType(i)==java.sql.Types.CHAR)
                       {
                           arow[i-1] =  rs.getString(i);
                       }
                       else if(metaData.getColumnType(i)==java.sql.Types.FLOAT 
                               || metaData.getColumnTypeName(i).equalsIgnoreCase("FLOAT"))
                       {
                           arow[i-1] =  rs.getFloat(i);
                       }
                       else if(metaData.getColumnType(i)==java.sql.Types.DOUBLE)
                       {
                           arow[i-1] =  rs.getDouble(i);
                       }
                       else if(metaData.getColumnType(i)==java.sql.Types.DATE)
                       {
                           arow[i-1] =  rs.getDate(i);
                       }
                       else if(metaData.getColumnType(i)==java.sql.Types.TIME)
                       {
                           arow[i-1] =  rs.getTime(i);
                       }
                       else if(metaData.getColumnType(i)==java.sql.Types.TIMESTAMP)
                       {
                           arow[i-1] =  rs.getTimestamp(i);
                       }
                       else
                       {
                              arow[i-1] = rs.getObject(i); 
                       }
                       
                   }
                    model.addRow(arow);
                }
                if(rs!=null) {
                rs.close();
                st.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDbTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        tca.adjustColumns();
            
    }

    public void deleteRows() {
        int[] sel = this.getSelectedRows();

        if (sel.length == 0) {
            return;
        }

        //Custom button text
        Object[] options = {"No", "Si"};
        String missatge = "Voleu suprimir " + sel.length + " files?";

        int n = JOptionPane.showOptionDialog(null,
                missatge, "Confirma esborrar",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (n != 1) {
            return;
        }
       
       isListening = false;

       int numdel = 0;
       for (int i : sel) {
           
           //Itera sobre totes les PK que contengui la taula
            String pkCondition = "";
            String mapIdentifier = "";
            for(int k=0; k<model.getColumnCount(); k++)
            {
                if(descriptors.get(k).isPrimaryKey())
                {
                String pkname = descriptors.get(k).getName();
                Object id = this.getValueAt(i-numdel, k);
                if(!pkCondition.isEmpty()) {
                        pkCondition += " AND ";
                    }
                pkCondition += pkname + "='"+id+"' ";
                mapIdentifier += id+";";
                }
            }
                 
            if(changes.containsKey(mapIdentifier)) {
                changes.remove(mapIdentifier);
            }
            
            String SQL1 = "DELETE FROM `"+catalog+"`." + tableName + " WHERE " + pkCondition;
            //System.out.println("DELETE QUERY = "+SQL1);
            int nup = mysql.executeUpdate(SQL1);
            if (nup > 0) {
                model.removeRow(i-numdel);
                numdel += 1;
            }
        }
       isListening = true;
    }

    public boolean isHasPK() {
        return !mapPKs.isEmpty();
    }
    
    
    public int commitChangesToDB()
    {
        int nchanges = 0;
        if(changes==null || changes.isEmpty()) {
            return nchanges;
        } //Nothing to be done 
        
        for(Object keyId: changes.keySet())
        {
            PreparedUpdate pud = changes.get(keyId);
           // //System.out.println("The update query is "+pud.query);
           // //System.out.println("The data is:");
            for(int i=0; i<pud.row.length; i++) {
                //System.out.println(pud.row[i]);
            }
            nchanges += mysql.preparedUpdate(pud.query, pud.row);
            String error = mysql.getLastError();
            if(error!=null && !error.isEmpty())
            {
                JOptionPane.showMessageDialog(null, error);
            }
        }
        
        return nchanges;
    }
}
