//ConditionP.java

package sde.monitor;

public interface ConditionP{
	public void Wait( int priority );
	public void Signal();
}//ConditionP