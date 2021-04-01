import java.util.List;

public class Workstation extends Entity {
	private Component first = null;
	private Component second = null;

	public Workstation(int assignedProduct) {
		super(assignedProduct);
	}
		
	public Event assemble() throws InterruptedException {
		first = null;
		second = null;
		return new Event(Event.types.ASSEMBLE, 100); //arbitrary wait time
	}

	@Override
	public synchronized void receive(Component component) throws InterruptedException {
		if(first != null) {
			first = component;
			
			if(this.getId() < 2) {
				assemble();
			}
		} else {
			second = component;
			
			assemble();
		}		
	}

	public void addBuffer(Buffer b) {
		this.assignedBuffers.add(b);
	}
}
