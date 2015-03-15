/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miobooktradeyp;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 *
 * @author antonio
 */
public class BookSellerAgent extends Agent{

    @Override
    protected void setup() {
        System.out.println("Seller: "+getAID().getName()+" attivo");
        //Creo la descrizione
        DFAgentDescription  description = new DFAgentDescription();
        description.setName(getAID());
        ServiceDescription ser_description = new ServiceDescription();
        ser_description.setType("book-selling");
        ser_description.setName("JADE-book-trading");
        description.addServices(ser_description);
        try{
            DFService.register(this, description);
        }catch(Exception e){}
                
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (Exception e) {
        }
    }

}
