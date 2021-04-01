import java.util.Queue;

public class Buffer {
	private int assignedComponent;
	Queue<Component> components;
	private final int capacity = 2;

	private boolean isOccupied;
	
	public Buffer(int assignedComponent) {
		this.assignedComponent = assignedComponent;
		setOccupied(false);
	}
	
	public synchronized Component pop() {
		return components.poll();
	}

	public synchronized void receive(Component c) throws InterruptedException {
		if(!this.isOccupied()) {
			components.add(c);
			if(components.size() == capacity) {
				setOccupied(true);
			};
//			if(!assignedWorkstation.isOccupied()) {
//				assignedWorkstation.receive(c);
//			} else {
//				this.wait();
//			}
		} else {
			this.wait();
		}
	}

	public int getAssignedComponent() {
		return assignedComponent;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}
}
