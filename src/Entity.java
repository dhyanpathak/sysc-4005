public abstract class Entity {
	private int id;
	private boolean occupied;
	
	public Entity(int id, boolean occupied) {
		this.id = id;
		this.occupied = occupied;
	}
	
	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public int getId() {
		return id;
	}

	public abstract void receive(Component component) throws InterruptedException;
}
