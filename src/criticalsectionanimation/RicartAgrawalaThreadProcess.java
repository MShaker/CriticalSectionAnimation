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
* Thread Process <br>
*
* Extends ThreadProcess and has each processes maintaining mutual exclusion 
* using the Ricart-Agrawala algorithm
*
* <br> <br>
* Created: <br>
* 2/2/2018, Sharif Shaker<br>
*
* @author Sharif Shaker
* @version 2/4/2018
*/
public class RicartAgrawalaThreadProcess extends ThreadProcess {
    
    private final PriorityBlockingQueue<Message> defferedReply = new PriorityBlockingQueue<>();
    private Message currentRequest = null;
    
    public RicartAgrawalaThreadProcess(int id, int speed, int xPos, int yPos, Color c, ArrayList qList, ArrayBlockingQueue in){
        super(id,speed,xPos,yPos,c,qList,in);
    }

    @Override
    protected void step() {
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
            
        } else if(replyCount == (outQueueList.size() - 1)){
            
            // if a reply was recieved from all processes at head of waiting queue indicate that process is in the CS
            inCS = true;
            waiting = false;
            currentRequest = null;
                    
        } else if(!waiting && !inCS && !defferedReply.isEmpty()){
            
            // if a reply is queued and process not waiting on CS respond to one of the deffered requests
            try {
                sendReply(defferedReply.take());
            } catch (InterruptedException ex) {
                Logger.getLogger(RicartAgrawalaThreadProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    @Override
    protected void readMessage() throws InterruptedException {
        if(!readInQ.isEmpty()){
            
            Message m = readInQ.take();
            
            switch(m.getMessageType()){
                case REQUEST:
                    // if received a request send a reply 
                    if((!waiting && !inCS) || (currentRequest.compareTo(m) == 1)){
                        // only if not waiting for CS or requested priority is greater
                        // current process priority
                        sendReply(m);
                    } else {
                        // otherwise defer reply until CS is available 
                        defferedReply.put(m);
                    }
                    break;
                case REPLY:
                    replyCount++; // increment replay count
                    break;
            }
        
        }
    }

    @Override
    protected void sendRequest() {
        synchronized(outQueueList){
           
            long timestamp = new Date().getTime(); // get timestamp to mark when message was sent
            
            currentRequest = new Message(processId, MessageType.REQUEST, timestamp); // create request message
            
            // send request to all processes
            for(int i = 0; i < outQueueList.size(); i++){
                if(i != processId){
                    try {
                        outQueueList.get(i).put(currentRequest);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadProcess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            // print update 
            System.out.println(" PROCESS " + processId + " sent a REQUEST to enter critical at time " + timestamp);
        }
    }

    @Override
    protected void sendReply(Message m) {
        
        synchronized(outQueueList){
            
            long timestamp = new Date().getTime(); // get time of message sent
            
            if(!outQueueList.isEmpty()){ 

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
}
