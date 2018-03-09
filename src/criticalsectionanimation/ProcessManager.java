/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalsectionanimation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
* Process Manager <br>
*
* Process color and speed manager
*
* <br> <br>
* Created: <br>
* 2/2/2018, Sharif Shaker<br>
*
* @author Sharif Shaker
* @version 2/2/2018
*/
public class ProcessManager extends JPanel {
    
    private Simulator sim;
    private JLabel speedLabel;
    private JComboBox speedSelect; 
   
    private JLabel colorLabel;
    private JComboBox colorSelect;
    private JLabel processLabel;
    private JComboBox idSelect;
 
    private ArrayList<ThreadProcess> pList;
    
    GridBagConstraints c;
            
    private final String[] colorList = {"","BLACK", "BLUE", "CYAN", 
        "DARK GRAY", "GRAY", "GREEN", "LIGHT GRAY",
        "MAGENTA", "ORANGE", "PINK", "RED", "WHITE",
        "YELLOW"};
    
    private final Integer[] idList = {0,1,2,3,4,5,6,7,8,9,10,11,12};
    
    private final String[] speedList = {"","RANDOM","1","2","3","4","5","6","7","8"};
    
    /**
     * constructor to place GUI elements into JPanel
     * @param simulator simulation object
     */
    public ProcessManager(Simulator simulator){
        sim = simulator;
        pList = sim.getProcesses();
        
        this.setLayout(new GridBagLayout());
        
        c = new GridBagConstraints();
        
        
        processLabel = new JLabel("Process ID: ");
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        this.add(processLabel, c);
        
        idSelect = new JComboBox(idList);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 0;
        this.add(idSelect, c);
        
        speedLabel = new JLabel("Speed: ");
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        this.add(speedLabel, c);
        
        speedSelect = new JComboBox(speedList);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        this.add(speedSelect, c);
        
        colorLabel = new JLabel("Color: ");
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        this.add(colorLabel, c);
        
        colorSelect = new JComboBox(colorList);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        this.add(colorSelect, c);
        
        
        
        
        
        initListeners();
        
    }
    
    
    /**
     * Initialize listeners on GUI objects 
     */
    public void initListeners(){
        
        // set speed selector logic
        speedSelect.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(pList.isEmpty() || (int)idSelect.getSelectedItem() > pList.size()-1){
                   speedSelect.setSelectedIndex(0); // set selector to default if animation not running
                } else {
                    ThreadProcess p = pList.get((int)idSelect.getSelectedItem());
                    switch((String)speedSelect.getSelectedItem()){ // set processes speed to selected value
                        case "1":
                            p.setSpeed(1);
                            break;
                        case "2":
                            p.setSpeed(2);
                            break;
                        case "3":
                            p.setSpeed(4);
                            break;
                        case "4":
                           p.setSpeed(5);
                           break;
                        case "5":
                           p.setSpeed(10);
                           break;
                        case "6":
                           p.setSpeed(20);
                           break;
                        case "7":
                           p.setSpeed(25);
                           break;
                        case "8":
                           p.setSpeed(50);
                           break;
                        case "RANDOM":
                            // speed must be divisable by 100 to remain on track
                            Random rand = new Random();
                            int speed = 3;
                            while(100%speed != 0){
                                speed = rand.nextInt(50) + 1; // get random number 
                            }
                            
                            p.setSpeed(speed);
                            
                            
                    }
                    
                    // output message if speed changed
                    if(!((String)speedSelect.getSelectedItem()).equals("")){
                        System.out.println(" SPEED of PROCESS " + (int)idSelect.getSelectedItem() +
                                " changed to " + (String)speedSelect.getSelectedItem());
                    }
                }
               
            }
        });
        
        // set color selector logic
        colorSelect.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                
                if(pList.isEmpty() || (int)idSelect.getSelectedItem() > pList.size()-1){
                   colorSelect.setSelectedIndex(0);// set selector to default if animation not running
                } else {
                   ThreadProcess p = pList.get((int)idSelect.getSelectedItem());
                    switch((String)colorSelect.getSelectedItem()){  // set processes color to selected value
                        case "BLUE":
                            p.setColor(Color.BLUE);
                            break;
                        case "RED":
                            p.setColor(Color.RED);
                            break;
                        case "WHITE":
                            p.setColor(Color.WHITE);
                            break;
                        case "LIGHT GRAY":
                            p.setColor(Color.LIGHT_GRAY);
                            break;
                        case "GREEN":
                            p.setColor(Color.GREEN);
                            break;
                        case "CYAN":
                            p.setColor(Color.CYAN);
                            break;
                        case "MAGENTA":
                            p.setColor(Color.MAGENTA);
                            break;
                        case "PINK":
                            p.setColor(Color.PINK);
                            break;
                        case "YELLOW":
                            p.setColor(Color.YELLOW);
                            break;
                        case "ORANGE":
                            p.setColor(Color.ORANGE);
                            break;
                        case "GRAY":
                            p.setColor(Color.GRAY);
                            break;
                        case "DARK GRAY":
                            p.setColor(Color.DARK_GRAY);
                            break;
                        case "BLACK":
                            p.setColor(Color.BLACK);
                    }
                    
                    // output that color was changed
                    if(!((String)colorSelect.getSelectedItem()).equals("")){
                        System.out.println(" COLOR of PROCESS " + (int)idSelect.getSelectedItem() +
                                " changed to " + (String)colorSelect.getSelectedItem());
                    }
                }
               
            }
        });
        
        // set id selector
        idSelect.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               // when id is changed reset color and speed select
               colorSelect.setSelectedIndex(0);
               speedSelect.setSelectedIndex(0);
               
            }
        });
    
    }
    
    public void resetSelections(){
        colorSelect.setSelectedIndex(0);
        speedSelect.setSelectedIndex(0);
        idSelect.setSelectedIndex(0);
    }
}
