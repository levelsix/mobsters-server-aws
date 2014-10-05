package org.springframework.integration.cluster.strictorder;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * A value object to hold entity lock information
 * @author David Turanski
 *
 */
public class LockNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String instanceInfo;
	private final String dispatcherName;
	private final String entityKey;

	
	@SuppressWarnings("unused")
	private LockNode(){throw new UnsupportedOperationException();}
	
	public LockNode(String entityKey, String lockName, String dispatcherName){
		this.entityKey = entityKey; 
		this.instanceInfo = lockName;
		this.dispatcherName = dispatcherName; 
	}
	
	public String getLockName() {
		return instanceInfo;
	}
	
 	public String getEntityKey() {
		return entityKey;
	}

	public boolean equals(Object other){
		if (null == other){
			return false;
		}
		
		if (!(other instanceof LockNode)){
			return false;
		}
		
		LockNode otherLockNode = (LockNode)other;
		return this.getKey().equals(otherLockNode.getKey());
	}
	
	public int hashCode(){
	   return getKey().hashCode();
	}
	
	public String toString(){
		return ("entityKey [" + entityKey + "] instanceInfo [" + instanceInfo + "] dispatcherName [" + dispatcherName + "]");
	}

	private static final String TO_STRING_REGEX = 
		"entityKey \\[([a-zA-Z][-._a-zA-Z0-9]*)\\] instanceInfo \\[([a-zA-Z][-._a-zA-Z0-9]*)\\] dispatcherName \\[([a-zA-Z][-._a-zA-Z0-9]*)\\]";
	private static final Pattern FROM_STRING_PATTERN = Pattern.compile(TO_STRING_REGEX);

	public static LockNode fromString(final String lockNodeDescription) 
	{
		if ((lockNodeDescription == null) || lockNodeDescription.isEmpty()) {
			throw new IllegalArgumentException( "Lock Node descriptor string can neither be null nor empty.");
		}
		
		final Matcher m = FROM_STRING_PATTERN.matcher(lockNodeDescription);
		if (! m.matches()) {
			throw new IllegalArgumentException(
				String.format("String argument, %s, is not a validly formatted lock node descriptor.", lockNodeDescription));
		}
		
		return new LockNode(
			m.group(1),
			m.group(2),
			m.group(3)
		);
	}

	public String getDispatcherName() {
		return dispatcherName;
	}
	
	public String getKey(){
		return entityKey + ":" + instanceInfo + ":" + dispatcherName;
	}
}
