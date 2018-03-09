/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalsectionanimation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
* Critical Section Main Frame <br>
*
* Frame object that contains all the graphical elements and runs the  
* animation loop
*
* <br> <br>
* Created: <br>
* 2/2/2018, Sharif Shaker<br>
*
* @author Sharif Shaker
* @version 2/2/2018
*/
public class CriticalSectionFrame extends JFrame {
    private Simulator sim;
    private TopControlPanel controlPanel;
    private ProcessManager processManager;
    
    
    /**
     * simple constructor that simply includes the name of the animation
     */
    public CriticalSectionFrame() {
        super("Critical Section Animation");
        
    } 
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
    }
 
 
    /**
     * Places various graphical elements into this from
     */
    public void initializeSimulation() {
        
        // setup frame
        this.setSize(1500, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        
        sim = new Simulator(); // create main simulator
        processManager = new ProcessManager(sim); // create lower processes manager
        controlPanel = new TopControlPanel(sim, processManager); // create top control panel 
        
        // create a text area 
        JTextArea output = new JTextArea(100,40);
        JScrollPane scroll = new JScrollPane(output);
        
        // set output messgages to write to the console
        MessageConsole mc = new MessageConsole(output);
        mc.redirectOut();
        mc.redirectErr(Color.RED, null);
        mc.setMessageLines(1000);   
        
        // add all elements to the frame
        this.add(processManager, BorderLayout.SOUTH);
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(sim, BorderLayout.CENTER);
        this.add(scroll, BorderLayout.WEST);
       
        // set frame to visible
        this.setVisible(true);

    }
    
    /**
     * Animation loop that continuously repaints GUI
     */
    public void runSimulation(){
        // main simulation loop
        while(true){
            // repaint the frame
            this.repaint();
            
           
        }
    }
}
