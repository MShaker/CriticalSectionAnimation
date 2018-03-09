/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalsectionanimation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
* Top Control Panel <br>
*
* Control panel for stopping, starting and pausing simulation  
*
* <br> <br>
* Created: <br>
* 2/2/2018, Sharif Shaker<br>
*
* @author Sharif Shaker
* @version 2/2/2018
*/
public class TopControlPanel extends JPanel {
    
    private JButton startBtn;
    private JButton pauseBtn;
    
    private final JLabel processesLabel;
    private JComboBox processNumComboBox;
    
    private final JLabel algorithmLabel;
    private JComboBox algorithmSelect;
    
    
    private ProcessManager processManager;
    
    private int numProcesses;

    private Simulator sim;
    
    
    /**
    * @param simulator simulator object on whith the animation is being run
    */
    public TopControlPanel(Simulator simulator, ProcessManager pm){
     
        processManager = pm;
        startBtn = new JButton("START");
        pauseBtn = new JButton("PAUSE");
        processesLabel = new JLabel("Number of Processes:");
        
        Integer[] processList = {1,2,3,4,5,6,7,8,9,10,11,12,13};
        processNumComboBox = new JComboBox(processList);
        processNumComboBox.setSelectedIndex(3);
        numProcesses = 4;
        
        String[] algorithms = {"LAMPORT'S", "RICART-AGRAWALA"};
        algorithmSelect = new JComboBox(algorithms);
        algorithmLabel = new JLabel("Algorithm:");
        
        sim = simulator;
        
        this.add(processesLabel);
        this.add(processNumComboBox);
        this.add(startBtn);
        this.add(pauseBtn);
        this.add(algorithmLabel);
        this.add(algorithmSelect);
        
        
        initListners();
        
        
    } 
    
    /**
    * initialize the button and selector action listeners
    */
    private void initListners(){
        
        
        startBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(startBtn.getText().equals("START")){ 
                    
                    sim.startProcesses(numProcesses, (String)algorithmSelect.getSelectedItem()); // start processes if simulator not starts
                    startBtn.setText("CLEAR"); // set button to clear while rocesses running
                    System.out.println(" SIMULATION STARTED WITH " + numProcesses + " PROCESSES."); // print update
                    System.out.println("    ALGORITHM --> " + (String)algorithmSelect.getSelectedItem() + ":\n");
                } else {
                    if(sim.isRunning()){ // if simulation is running
                        sim.pauseResumeProcesses(); // pause processes
                    }
                    sim.stopProcesses(); // stop processes 
                    processManager.resetSelections();
                    startBtn.setText("START");
                    System.out.println("\n SIMULATION ENDED\n");
                }
                
                pauseBtn.setText("PAUSE");
               
            }
        });
        
        pauseBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                
                if(startBtn.getText().equals("START")) return; // do nothing if animation not running
                    
                
                if(pauseBtn.getText().equals("PAUSE")){
                    pauseBtn.setText("RESUME");
                    System.out.println("\n SIMULATION PAUSED...");
                } else {
                    pauseBtn.setText("PAUSE");
                    System.out.println("\n ...SIMULATION RESUMED\n");
                }
               
                // pause or resume processes depending on current state of animaiton
                sim.pauseResumeProcesses(); 
            }
        });
        
        processNumComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               // change the number of processes to match the number in the combobox
               numProcesses = (Integer)processNumComboBox.getSelectedItem();
               
            }
        });
    }
    
    
    
    
    
}
