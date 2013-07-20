/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.mysqlbrowser.wizard;

import org.iesapp.apps.mysqlbrowser.util.wizard.WizardPanelDescriptor;



public class StepDescriptor3 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP3";
    private final Step3 panel3;
    
    public StepDescriptor3() {
        panel3= new Step3();
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel3);
    }
    
     @Override
    public void displayingPanel() {
       Step2 panel2 = (Step2) getWizard().getModel().getPanelwithIdDescriptor("STEP2");
       panel3.onShow(panel2.getSelectedSheet(), panel2.getAssociations());
    }
        
   @Override
    public void aboutToHidePanel() {
       if(getWizard().getReturnCode()==0) {
            panel3.doSave();
        }
    }
 
    @Override
    public Object getBackPanelDescriptor() {
        return StepDescriptor2.IDENTIFIER;
 
    }  

    @Override
     public Object getNextPanelDescriptor() {
         return FINISH;
    }
    
    
}
