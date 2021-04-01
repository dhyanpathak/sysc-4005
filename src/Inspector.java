import java.util.List;

public class Inspector extends Entity {

	public Inspector(int id) {
		super(id);
	}
	
	public synchronized Event inspect(Component c) throws InterruptedException {
		return new Event(Event.types.PROCESS, 50); //arbitrary wait time
	}
	
	@Override
	public void receive(Component c) throws InterruptedException {
		if(!this.isOccupied()) {
			this.setOccupied(true);
			inspect(c);
			
			for(int i = 0; i < assignedBuffers.size(); i++) {
				if(assignedBuffers.get(i).getAssignedComponent() == c.getType()) {
					assignedBuffers.get(i).receive(c);
					break;
				}
			}
		} else {
			this.wait();
		}
	}

	public void addBuffer(Buffer b) {
		assignedBuffers.add(b);
	}
}
