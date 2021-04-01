import java.util.ArrayList;
import java.util.List;

public abstract class Entity extends Thread {
	private long id;
	private boolean occupied;
	List<Buffer> assignedBuffers;

	public Entity(long id) {
		this.id = id;
		this.occupied = false;
		assignedBuffers = new ArrayList<>();
	}
	
	public synchronized boolean isOccupied() {
		return occupied;
	}

	public synchronized void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public long getId() {
		return id;
	}

	public abstract void receive(Component component) throws InterruptedException;
}
