/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalsectionanimation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import javax.swing.JPanel;

/**
* Simulator <br>
*
* Manages the starting, stopping and painting of the simulation
*
* <br> <br>
* Created: <br>
* 2/2/2018, Sharif Shaker<br>
*
* @author Sharif Shaker
* @version 2/2/2018
*/
public class Simulator extends JPanel {
    
   
    private final ArrayList<ThreadProcess> processes;
    private final ArrayList<ArrayBlockingQueue> queueList;
    
    private boolean running = true;
    
    /**
     * initialize communication queue and list of processes
     */
    public Simulator(){
        
        queueList = new ArrayList<>();
        processes = new ArrayList<>();
       
    }
    
    /**
     * start simulation animation with given number of processes
     * @param numProcesses number of process to include in the simulation
     */
    public void startProcesses(int numProcesses, String algorithm){
        
        running = true; // set running flag to true
        Color c;
        int speed;
        Random rand = new Random(); // random generator for getting random speed
        int x;
        int y;
        
        // ensure number of processes is between 1 and 13
        if(numProcesses > 13){ 
            numProcesses = 13;
        } else if(numProcesses < 1){
            numProcesses = 1;
        } 
        
        // create a communication queue for each process and place in list 
        for (int i = 0; i < numProcesses; i++){
            queueList.add(new ArrayBlockingQueue<>(4*numProcesses));
        }
        
        for (int i = 0; i < numProcesses; i++){
            
            // set processes color and location based on its process number 
            switch(i){
                case 0:
                    c = Color.BLUE;
                    x = 200;
                    y = 300;
                    break;
                case 1:
                    c = Color.RED;
                    x = 200;
                    y = 700; 
                    break;
                case 2:
                    c = Color.WHITE;
                    x = 800;
                    y = 300;
                    break;
                case 3:
                    c = Color.LIGHT_GRAY;
                    x = 800;
                    y = 700;
                    break;
                case 4:
                    c = Color.GREEN;
                    x = 200;
                    y = 300;
                    break;
                case 5:
                    c = Color.CYAN;
                    x = 200;
                    y = 700;
                    break;
                case 6:
                    c = Color.MAGENTA;
                    x = 800;
                    y = 300;
                    break;
                case 7:
                    c = Color.PINK;
                    x = 800;
                    y = 700;
                    break;
                case 8:
                    c = Color.YELLOW;
                    x = 200;
                    y = 300;
                    break;
                case 9:
                    c = Color.ORANGE;
                    x = 200;
                    y = 700;
                    break;
                case 10:
                    c = Color.GRAY;
                    x = 800;
                    y = 300;
                    break;
                case 11:
                    c = Color.DARK_GRAY;
                    x = 800;
                    y = 700;
                    break;
                default:
                    c = Color.BLACK;
                    x = 200;
                    y = 300;
            }
            
            speed = 3; 
            while(100%speed != 0){ // make sure random speed is divisible by 100
                speed = rand.nextInt(50) + 1; 
            }
            
            switch(algorithm){
                case "LAMPORT'S":
                    processes.add(new LamportThreadProcess(i,speed, x, y , c, queueList, queueList.get(i)));
                    break;
                case "RICART-AGRAWALA":
                    processes.add(new RicartAgrawalaThreadProcess(i,speed, x, y , c, queueList, queueList.get(i)));
                    break;
            }
            // create the process and add it to the process list
            
            
        }
        
        // start all the threads
        for(ThreadProcess p : processes){
            Thread t = new Thread(p);
            t.start();
        }
        
    }
    
    /**
     * stops threads and and clears communication queue and processes list 
     */
    public void stopProcesses(){
        
        running = false;
        for(ThreadProcess p : processes){
            p.endProcess();
        }
        
        processes.clear();
        queueList.clear();
    }
    
    /**
     * pause processes is active or resume them if paused
     */
    public void pauseResumeProcesses(){
        for(ThreadProcess p : processes){
            p.pauseResumeProcess();
        }
        
    }
    
    
    
     // draws lines to represent track 
    private void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(400, 500, 200, 700);
        g2d.drawLine(400, 500, 200, 300);
        
        g2d.drawLine(600, 500, 800, 700);
        g2d.drawLine(600, 500, 800, 300);
        
        g2d.drawLine(200, 700, 200, 300);
        g2d.drawLine(800, 700, 800, 300);
        
        g2d.setStroke(new BasicStroke(4)); // color CS red
        g2d.setColor(Color.RED);
        g2d.drawLine(400, 500, 600, 500); // critical section
 
    }
    
    /**
     * paint animation elements
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
     
        // redraw lines
        drawLines(g);
        
        if(running){ // if animation is active
            // for each process 
            for(ThreadProcess p:processes){
                // update graphics
                g2d.setColor( p.getColor() );
                g2d.fillOval(p.getX()-15, p.getY()-15, 30, 30);
                
                // include a number label with each process
                g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
                g2d.setColor(Color.BLACK);
                g2d.drawString(""+p.getProcessId(),p.getX(),p.getY() - 20); 
            }
        }
        
        
    }
    
    /**
     * 
     * @return true if animation is running
     */
    public boolean isRunning(){
        return running;
    }
    
    /**
     * @return list of process objects
     */
    public ArrayList getProcesses(){
        return processes;
    }
    
    /**
     * @return list of message queues;
     */
    public ArrayList getQueueList(){
        return queueList;
    }
    
}
