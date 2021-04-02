import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.Thread;

public abstract class Entity extends Thread {
	private long id;
	private boolean occupied;
	private volatile boolean running;
	private long serviceStartTime = 0;
	private long occupiedStartTime = 0;
	List<Buffer> assignedBuffers;

	private HashMap<Integer, Double> waitTimes = new HashMap<Integer, Double>();

	public Entity(long id) {
		this.id = id;
		this.occupied = false;
		assignedBuffers = new ArrayList<>();
		running = true;
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

	public double getWaitTime(int i) {
		return waitTimes.get(i);
	}

	public void setWaitTime(int i, double waitTime) {
		this.waitTimes.put(i, waitTime);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public long getServiceStartTime() {
		return serviceStartTime;
	}

	public void setServiceStartTime(long serviceStartTime) {
		this.serviceStartTime = serviceStartTime;
	}

	public long getOccupiedStartTime() {
		return occupiedStartTime;
	}

	public void setOccupiedStartTime(long occupiedStartTime) {
		this.occupiedStartTime = occupiedStartTime;
	}


}
