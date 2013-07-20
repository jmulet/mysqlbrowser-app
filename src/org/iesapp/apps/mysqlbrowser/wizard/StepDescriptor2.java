/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.mysqlbrowser.wizard;

import org.iesapp.apps.mysqlbrowser.util.wizard.WizardPanelDescriptor;


public class StepDescriptor2 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP2";
    private final Step2 panel2;
    
    public StepDescriptor2() {
       panel2= new Step2();

        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel2);
    }
    
     @Override
    public void displayingPanel() {
        Step1 panel1 = (Step1) getWizard().getModel().getPanelwithIdDescriptor("STEP1");
        panel2.setExcelFileName(panel1.getSelectedFile());
    }
        
   @Override
    public void aboutToHidePanel() {
        
    }
 
    @Override
    public Object getBackPanelDescriptor() {
        return StepDescriptor1.IDENTIFIER;
 
    }  

    @Override
     public Object getNextPanelDescriptor() {
          return StepDescriptor3.IDENTIFIER;
    }
    
    
}
