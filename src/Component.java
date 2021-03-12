public class Component {
	private int type;
	
	public Component(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public void arriveTo(Entity destination) throws InterruptedException {
		destination.receive(this);
	}
}
