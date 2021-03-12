import java.util.List;

public class Buffer extends Entity {
	List<Component> components;
	Workstation assignedWorkstation;
	private final int capacity = 2;
	
	public Buffer(int id, boolean occupied) {
		super(id, occupied);
	}
	
	public Component pop() {
		if(components.size() > 0) {
			this.setOccupied(false);

			return components.remove(0);	
		} else {
			return null;
		}
	}
	
	@Override
	public void receive(Component c) throws InterruptedException {
		if(!this.isOccupied()) {
			components.add(c);
			if(components.size() == capacity) {
				this.setOccupied(true);
			};
			if(!assignedWorkstation.isOccupied()) {
				assignedWorkstation.receive(c);
			} else {
				this.wait();
			}
		} else {
			this.wait();
		}
	}
}
