package csm.exception;

public class RouterException extends RuntimeException {
	
	public static final String EXC_PROBABILITA = "Parametri probabilita devono sommare a 1";
	public static final String EXC_STAZIONI = "Numero di stazioni e probabilità devono coincidere";
	
	public RouterException(String message){
		super(message);
	}
}
