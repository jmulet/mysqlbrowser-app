/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.mysqlbrowser.wizard;

 
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.iesapp.apps.mysqlbrowser.util.wizard.WizardPanelDescriptor;
import org.iesapp.database.MyDatabase;

/**
 *
 * @author Josep
 */
public class ExcelImportWiz extends org.iesapp.apps.mysqlbrowser.util.wizard.Wizard {
    public static MyDatabase mysql;
    public static String tableName;
    
     
    public ExcelImportWiz(MyDatabase mysql, String tableName)       
    {
        ExcelImportWiz.mysql = mysql;
        ExcelImportWiz.tableName = tableName;
         
        this.getDialog().setTitle("Assistent d'importació EXCEL");
        
        WizardPanelDescriptor descriptor1 = new StepDescriptor1();
        this.registerWizardPanel(StepDescriptor1.IDENTIFIER, descriptor1);
        
        WizardPanelDescriptor descriptor2 = new StepDescriptor2();
        this.registerWizardPanel(StepDescriptor2.IDENTIFIER, descriptor2);
   
        WizardPanelDescriptor descriptor3 = new StepDescriptor3();
        this.registerWizardPanel(StepDescriptor3.IDENTIFIER, descriptor3);
        
        this.setCurrentPanel(StepDescriptor1.IDENTIFIER);
 
    }
    
    public static void showQuickHelpDialog(JFrame parent, String txt) {
		// create and configure a text area - fill it with exception text.
		final JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
		textArea.setEditable(false);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
		textArea.setText(txt);
		
		// stuff it in a scrollpane with a controlled size.
		JScrollPane scrollPane = new JScrollPane(textArea);		
		scrollPane.setPreferredSize(new Dimension(350, 150));
		
		// pass the scrollpane to the joptionpane.				
		JOptionPane.showMessageDialog(parent, scrollPane, "Ajuda", JOptionPane.INFORMATION_MESSAGE);
	}
}
