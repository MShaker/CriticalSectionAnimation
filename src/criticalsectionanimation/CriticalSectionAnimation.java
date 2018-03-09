/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalsectionanimation;


/**
* Critical Section Animation <br>
*
* Main class that sets up a critical section animation frame object that 
* runs the simulation
*
* <br> <br>
* Created: <br>
* 2/2/2018, Sharif Shaker<br>
*
* @author Sharif Shaker
* @version 2/2/2018
*/
public class CriticalSectionAnimation {

    
    /**
     * main method creates and runs the animation
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        CriticalSectionFrame frame = new CriticalSectionFrame(); 
        
        frame.initializeSimulation(); 
        frame.runSimulation();
        

    }
       
    
}
