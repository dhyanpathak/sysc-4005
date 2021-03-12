
public class Event {
	enum types {
		PROCESS,
		ASSEMBLE
	};
	
	private types type; 
	
	public Event(types type, int waitTime) throws InterruptedException {
		if(type.equals(types.ASSEMBLE)) {
			this.wait(waitTime);
		}
	}

}
