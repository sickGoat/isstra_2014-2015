package csm.util;

public class CSM_Constraints {
	
	/**
	 * Parametri distribuzioni
	 */
	//Stazione di riflessione
	public static final double LAMBDA_RIFLESSIONE = 0.01;
	//Stazione s1 ( esponenziale ) 
	public static final double LAMBDA_S1 = 2.3;
	//Stazione s2 ( esponenziale )
	public static final double LAMBDA_S2 = 0.8;
	//Stazione s3 ( erlang )
	public static final double 	LAMBDA_S3 = 0.6;
	public static final int		EARLANG_N = 16;
	//Stazione s4 ( iper-exp )
	public static final double [] PROB_IPER = { 0.95, 0.05 };
	public static final double [] LAMB_IPER = { 5 , 0.5 };
								
	/**
	 * Probabilita instradamenti router
	 */
	public static final double[] PROB_INSTRADAMENTI = { 0.2, 0.3, 0.2, 0.3 };
	
	/**
	 * Durata simulazione
	 */
	public static final double tEND = 30000000;
	
	/**
	 *Numero popolazione 
	 */
	public static final int N_POP = 5;
							
}
