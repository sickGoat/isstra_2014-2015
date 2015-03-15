/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw;

import jade.core.Agent;

/**
 *
 * @author antonio
 */
public class HelloWorldAgent extends Agent {
        
    protected void setup(){
        System.out.println("Hello World!I'm "+getAID().getName());
        doDelete();
    }
    
    protected void takeDown(){ System.out.println("Bye");}
}
