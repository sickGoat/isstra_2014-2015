/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arith;

import jade.core.Agent;

/**
 *
 * @author antonio
 */
public class Calculator extends Agent{

@Override
protected void setup(){
    System.out.println("Simple Calculator");
    Object[] argomenti = getArguments();
    if(argomenti == null || argomenti.length != 3 ){ System.out.println("Argomenti sbagliati"); doDelete();}
    else{
        double a = Double.parseDouble(argomenti[0].toString());
        double b = Double.parseDouble(argomenti[1].toString());
        char operatore = argomenti[2].toString().charAt(0);
        double risultato = 0;
        boolean ok = true;
        switch( operatore ){
            case '+':
                risultato = a+b;
                break;
            case '-':
                risultato = a-b;
                break;
            case '*':
                risultato = a*b;
                break;
            case '/':
                risultato = a/b;
                break;
            default:
                System.out.println("Operatore sconosciuto");ok = false;
        }
        System.out.println("risultato = " + risultato);
        doDelete();
    }
}

@Override
protected void takeDown(){ System.out.println("Calculator si spegne!");}

}
