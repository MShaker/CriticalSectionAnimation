/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalsectionanimation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Lamport's Thread Process <br>
*
* Extends ThreadProcess and has each processes maintaining mutual exclusion 
* using Lamport's algorithm
*
* <br> <br>
* Created: <br>
* 2/2/2018, Sharif Shaker<br>
*
* @author Sharif Shaker
* @version 2/4/2018
*/
public class LamportThreadProcess extends ThreadProcess {
    
    private final PriorityBlockingQueue<Message> waitingList = new PriorityBlockingQueue<>(); 
    
    public LamportThreadProcess(int id, int speed, int xPos, int yPos, Color c, ArrayList qList, ArrayBlockingQueue in){
        super(id, speed, xPos, yPos, c, qList, in);
    }
    
     /**
    * contains the logic for a single step of a processes
    */
    @Override
    protected void step(){
        
        checkSpeed(); // check that speed is up to date 
        
        if(paused) return; // if process is paused dont step
        
        if(((x == 400 && right) || (x == 600 && !right)) && !waiting){
            // set process to waiting and send request if about to enter critical section 
            waiting = true;
            sendRequest();
            
        } else if(((x == 400 && !right) || (x == 600 && right)) && inCS){
            // if leaving CS send realease to other processes and indicate
            inCS = false;
            replyCount = 0; // reset reply count 
            sendRelease();
        } else if(replyCount == (outQueueList.size() - 1) && !waitingList.isEmpty() &&  waitingList.peek().getSenderId() == processId && waiting){
            // if a reply was recieved from all processes at head of waiting queue indicate that process is in the CS
            inCS = true;
            waiting = false;
                    
        } else {
            
            try {
                readMessage(); // read queues to determine action
                
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadProcess.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
        if(!waiting){
            moveProcess(); // if not waiting for CS move the process
        }
        
    }
    
    /**
    * reads the next message in message queue and perform necessary actions 
    * @throws InterruptedException 
    */
    @Override
    protected void readMessage() throws InterruptedException  {
         
        if(!readInQ.isEmpty()){
            
            Message m = readInQ.take();
        
            switch(m.getMessageType()){
                case REQUEST:
                    // if received a request send a reply 
                    sendReply(m);
                    break;
                case REPLY:
                    replyCount++; // increment replay count 
                    break;
                case RELEASE:
                    waitingList.take(); // if CS was released remove process form waiting queue
                    break;
            }
        
        }
        
    }
    
    /**
    * send request to enter CS to all other processes
    */
    @Override
    protected void sendRequest(){
        
        synchronized(outQueueList){
           
            long timestamp = new Date().getTime(); // get timestamp to mark when message was sent
            
            Message m = new Message(processId, MessageType.REQUEST, timestamp); // create request message
            waitingList.put(m);// put request in waiting list
            
            // send request to all processes
            for(int i = 0; i < outQueueList.size(); i++){
                if(i != processId){
                    try {
                        outQueueList.get(i).put(m);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadProcess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            // print update 
            System.out.println(" PROCESS " + processId + " sent a REQUEST to enter critical at time " + timestamp);
        }
    }
          
    /**
    * send reply to requesting message
     * @param m message that was sent
    */
    @Override
    protected void sendReply(Message m){
        
        synchronized(outQueueList){
            
            long timestamp = new Date().getTime(); // get time of message sent
            
            if(!outQueueList.isEmpty()){ 
                waitingList.put(m); // put message in waiting queue

                try {
                   // send reply to requestor
                   outQueueList.get(m.getSenderId()).put(new Message(processId, MessageType.REPLY, timestamp));
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadProcess.class.getName()).log(Level.SEVERE, null, ex);
                } 
            
            }
            
            // print update 
            System.out.println(" PROCESS " + processId + " sent a REPLY to PROCESS " + m.getSenderId() + " at time " + timestamp);
            
        }
    }   
    
    /**
    * release CS so others can use it
    */
    private void sendRelease(){
        synchronized(outQueueList){
            
            long timestamp = new Date().getTime(); // get time message was sent
            
            try {
                waitingList.take(); // take own process request off of waiting queue
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // inform all other processes that the CS is available
            for(int i = 0; i < outQueueList.size(); i++){
                if(i != processId && !outQueueList.isEmpty()){
                    try {
                        outQueueList.get(i).put(new Message(processId, MessageType.RELEASE, timestamp));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadProcess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            // print update
            System.out.println(" PROCESS " + processId + " RELEASED the critical section at time " + timestamp);
            
        }
    }
    
}
