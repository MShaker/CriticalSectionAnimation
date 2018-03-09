/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalsectionanimation;

import criticalsectionanimation.MessageType;

/**
* Message <br>
*
* Message object that allows processes to communicate important information
*
* <br> <br>
* Created: <br>
* 2/2/2018, Sharif Shaker<br>
*
* @author Sharif Shaker
* @version 2/2/2018
*/
public class Message implements Comparable<Message>{
    
    private int senderId;
    private MessageType messageType;
    private long timestamp;
  
    
    /**
     * Message constructor 
     * @param id identifier of message sender
     * @param mt type of message sent
     * @param time time message was sent
     * 
     */
    public Message(int id, MessageType mt, long time){
        senderId = id;
        messageType = mt;
        timestamp = time;
    }
    
    /**
     * get id of message sender
     * 
     */
    public int getSenderId(){
        return senderId;
    }
    
    /**
     * get time message was sent
     */
    public long getTimestamp(){
        return timestamp;
    }
    
    /**
     * get type of message
     */
    public MessageType getMessageType(){
        return messageType;
    }

    /**
     * given an array, turns the list into a max-heap
     * @param am message to compare
     * @return -1 if this message should take priority and 1 otherwise
     */
    @Override
    public int compareTo(Message am) {
       
        if(am.getTimestamp() < timestamp){ // if this message was sent later
            return 1;
        } else if(am.getTimestamp() > timestamp) {
            return -1;
        } else {
            // if messages came at same time prioritize based on processes id
            if(senderId < am.getSenderId()){
                return -1;
            } else {
                return 1;
            }
        }
    }
    
    
}
