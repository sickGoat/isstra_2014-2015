package sde.actor;

public class Timer extends Message{
   private Message timeout;
   private double  firetime;
   private static ControlMachine cm;
   private double set_time;
   public static void setRuntime( ControlMachine cm ){
      Timer.cm=cm;
   }//setRuntime
   public void set( Message timeout,
                    Actor destination,
                    double firetime ){
	  set_time=cm.now();
      timeout.receiver=destination;
      this.timeout=timeout;
      this.firetime=firetime+cm.now();
      cm.schedule( this );
   }//set
   public void reset(){
      cm.unSchedule( this );
   }//reset
   public double timeRemaining(){
      return firetime-cm.now();
   }//timeRemaining
   public double elapsedTime(){
	   return cm.now()-set_time;
   }//elapsedTime
   public double firetime(){ return firetime; }
   public Message timeout(){ return timeout; }
   //equals of Object
}//Timer
