import java.util.List;

public class Workstation extends Entity {
	private int assignedProduct;
	private Component first = null;
	private Component second = null;

	public Workstation(int assignedProduct, boolean occupied) {
		super(assignedProduct, occupied);
	}
		
	public Event assemble() throws InterruptedException {
		first = null;
		second = null;
		return new Event(Event.types.ASSEMBLE, 100); //arbitrary wait time
	}

	@Override
	public void receive(Component component) throws InterruptedException {
		if(first != null) {
			first = component;
			
			if(assignedProduct < 2) {
				assemble();
			}
		} else {
			second = component;
			
			assemble();
		}		
	}
}
