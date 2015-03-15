//file ControlMachine.java
package sde.actor;
public abstract class ControlMachine{
   public abstract void schedule( Message m );
   public abstract void unSchedule( Message m );
   public abstract void controller();
   public double now(){ return System.currentTimeMillis(); }
   public abstract void reset();
}//ControlMachine

