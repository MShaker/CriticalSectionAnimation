/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalsectionanimation;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Thread Process <br>
*
* Abstract object to represent a process running in a different thread 
*
* <br> <br>
* Created: <br>
* 2/2/2018, Sharif Shaker<br>
*
* @author Sharif Shaker
* @version 2/4/2018
*/
public abstract class ThreadProcess implements Runnable {
    
    protected final ArrayBlockingQueue<Message> readInQ;
    protected final List<ArrayBlockingQueue> outQueueList;
    
    protected final int processId;
    protected int x;
    protected int y;
    protected int stepSpeed, updateSpeed;
    protected Color color;
    protected boolean right;
    
    protected boolean inCS;
    protected int replyCount;
    protected boolean waiting;
    
    protected boolean processRunning;
    protected boolean paused;
    
    /**
     * Constructor for Thread Process object 
     * @param id process id 
     * @param speed speed of the process along the track
     * @param xPos position of process along the x axis
     * @param yPos position of process along the y axis 
     * @param c color of the process 
     * @param qList list of communication queues used between processes
     * @param in input queue for receiving messages from other processes 
     */
    public ThreadProcess(int id, int speed, int xPos, int yPos, Color c, ArrayList qList, ArrayBlockingQueue in){
        processId = id;
        readInQ = in;
        outQueueList = Collections.synchronizedList(qList);
        
        x = xPos;
        y = yPos;
        stepSpeed = speed;
        updateSpeed = speed;
        color = c;
        
        inCS = false;
        replyCount = 0;
        waiting = false;
        processRunning = true;
        paused = false;
    }

     /**
     * While simulation is running step the process forward in a new thread
     */
    @Override
    public void run() {
        while(processRunning){
             try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(CriticalSectionAnimation.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            step();
        }
    }  
    
    /**
    * Preforms logic for moving process along the track
    */
    protected void moveProcess(){
       
        if(y == 300 && x == 200){
            right = true;
            x+=stepSpeed;
            y+=stepSpeed;
        } else if(y == 700 && x == 800){
            right = false;
            x-=stepSpeed;
            y-=stepSpeed;
        } else if(x == 200){
            y-=stepSpeed;
        } else if (x == 800){
            y+=stepSpeed;
        } else if(x > 400 && x < 600){
            if(right) x+=stepSpeed;
            else x-=stepSpeed;
        } else if(x > 600){
            if(right){
                x+=stepSpeed;
                y-=stepSpeed;
            } else {
                x-=stepSpeed;
                y-=stepSpeed;
            }
        } else if (x < 400){
            if(right){
                x+=stepSpeed;
                y+=stepSpeed;
            } else {
                x-=stepSpeed;
                y+=stepSpeed;
            }
        } else if (x == 400){
            if(right){
                x+=stepSpeed;
            } else {
                x-=stepSpeed;
                y+=stepSpeed;
            }
        } else if (x == 600){
            if(right){
                x+=stepSpeed;
                y-=stepSpeed;
            } else {
                x-=stepSpeed;
            }
        }
       
    
    }
    
    /**
    * @return x position
    */
    public int getX(){
        return x;
    }
    
    /**
    * @return y position
    */
    public int getY(){
        return y;
    }
    
    /**
    * @return speed
    */
    public int getSpeed(){
        return stepSpeed;
    }
    
    /**
    * updates speed of process
    * @param speed to set processes to
    */
    public void setSpeed(int speed){
        updateSpeed = speed;
    }
    
    /**
    * check if speed is needs to be updated.  If process is in an invalid location
    * you cannot change its speed as it might step out of the track constraints
    */
    protected void checkSpeed(){
        
        // if the speed to be updated does not match the current speed
        if(updateSpeed != stepSpeed){
            if(x == 250 || x == 300 || x == 400 || x == 500 || x == 600 || x == 700
                    || ((x == 200 || x == 800) 
                    && (y == 300 || y == 400 || y == 500 || y == 600 || y == 700))){
                stepSpeed = updateSpeed; // if processes is in a valid position update speed
            }
        }
    }
    
    /**
    * @return color of process
    */
    public Color getColor(){
        return color;
    }
    
    /**
    * @param c color of processes
    */
    public void setColor(Color c){
        color = c;
    }
    
    /**
    * @return boolean to indicate direction processes is traveling
    */
    public boolean goingRight(){
        return right;
    }
    
    /**
    * ends process by setting running to false
    */
    public void endProcess(){
        processRunning = false;
    }
    
    /**
    * pause or resume process
    */
    public void pauseResumeProcess(){
        paused = !paused;
    }
    
    /**
    * @return id of process
    */
    public int getProcessId(){
        return processId;
    }
    
    /**
    * contains the logic for a single step of a processes
    */
    protected abstract void step();
    
    /**
    * reads the next message in message queue and perform necessary actions 
    * @throws InterruptedException 
    */
    protected abstract void readMessage() throws InterruptedException;
        
    
    /**
    * send request to enter CS to all other processes
    */
    protected abstract void sendRequest();
          
    /**
    * send reply to requesting message
    */
    protected abstract void sendReply(Message m); 
  
    
}
