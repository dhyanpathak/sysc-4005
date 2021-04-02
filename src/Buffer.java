import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
	private int assignedComponent;
	Queue<Component> components;
	private final int capacity = 2;

	private boolean isOccupied;
	
	public Buffer(int assignedComponent) {
		this.assignedComponent = assignedComponent;
		setOccupied(false);
		components = new LinkedList<Component>();
	}
	
	public synchronized Component pop() {
		while(components.size() == 0) {
			try {
				wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		Component c = components.poll();
		setOccupied(false);
		notifyAll();
		return c;
	}

	public synchronized void receive(Component c) throws InterruptedException {
		while(isOccupied()) {
			try {
				wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("Buffer for C" + c.getType() + " received component.");
		components.add(c);
		if(components.size() == capacity) {
			setOccupied(true);
		};
		notifyAll();
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
