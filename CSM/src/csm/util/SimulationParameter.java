/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csm.util;

/**
 *
 * @author antonio
 */
public class SimulationParameter {
    
    public int n_popolazione;
    public double lambda_router;
    public double lambda_sr;
    public int n_eralng;
    public double lambda_erlang;
    public double lambda_iper_1;
    public double lambda_iper_2;
    public double alfa_iper_1;
    public double alfa_iper_2;
    public double lambda_s2;
    public double tEnd;


    public static SimulationParameter getNuovaIstanza(SimulationParameter p){
        SimulationParameter p_clone = new SimulationParameter();
        p_clone.alfa_iper_1 = p.alfa_iper_1;
        p_clone.alfa_iper_2 = p.alfa_iper_2;
        p_clone.lambda_erlang = p.lambda_erlang;
        p_clone.lambda_iper_1 = p.lambda_iper_1;
        p_clone.lambda_iper_2 = p.lambda_iper_2;
        p_clone.lambda_router = p.lambda_router;
        p_clone.lambda_s2 = p.lambda_s2;
        p_clone.lambda_sr = p.lambda_sr;
        p_clone.tEnd = p.tEnd;
        p_clone.n_eralng = p.n_eralng;
        p_clone.n_popolazione = p.n_popolazione;
        
        return p_clone;
    }
    
}