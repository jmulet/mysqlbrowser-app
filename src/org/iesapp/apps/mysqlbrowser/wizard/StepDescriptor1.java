/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.mysqlbrowser.wizard;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.iesapp.apps.mysqlbrowser.util.wizard.WizardPanelDescriptor;


public class StepDescriptor1 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP1";
    private final Step1 panel2;

    public StepDescriptor1() {
        panel2 = new Step1();
        panel2.registerActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("ApproveSelection"))
                {
                     getWizard().setNextFinishButtonEnabled(true);
                }
//                else
//                {
//                    getWizard().interruptWizard();
//                }
            }
            
        });
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel2);
    }

    @Override
    public void displayingPanel() {
        getWizard().setNextFinishButtonEnabled(false);
    }

    @Override
    public void aboutToHidePanel() {
    }

    @Override
    public Object getBackPanelDescriptor() {
        return null;

    }

    @Override
    public Object getNextPanelDescriptor() {
        return StepDescriptor2.IDENTIFIER;
    }
}
