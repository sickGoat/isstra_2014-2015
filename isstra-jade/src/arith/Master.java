/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arith;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

/**
 *
 * @author antonio
 */
public class Master extends Agent{
    @Override
    protected void setup() {
        System.out.println("Dynamic agent creation");
        ContainerController cc = null;
        AgentController ac = null;
        try{
            cc = getContainerController();
            ac = cc.createNewAgent("calc", "arith.Calculator", new Object[]{"2","3","*"});
            ac.start();
        }catch(Exception e){ System.out.println("Errore durante la creazione");}
        doDelete();
    }
}
