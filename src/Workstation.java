import java.util.List;

public class Workstation extends Entity {
	private Component first = null;
	private Component second = null;

	public Workstation(int assignedProduct) {
		super(assignedProduct);
	}
		
	public void assemble() throws InterruptedException {
		this.setOccupied(true);

		first = null;
		second = null;
		sleep((long) (this.getWaitTime((int) this.getId()) * 1000));

		this.setOccupied(false);
		Tracking.getInstance().addServiceTime("workstation"+getId(), (double) (System.currentTimeMillis() - getServiceStartTime()));
	}

	@Override
	public void receive(Component component) throws InterruptedException {
		if(getOccupiedStartTime() == 0) {
			setOccupiedStartTime(System.currentTimeMillis());
		}

		while(isOccupied()) {
			try {
				wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		Tracking.getInstance().addOccupiedTime("workstation"+getId(), (double) (System.currentTimeMillis() - getOccupiedStartTime()));
		setOccupiedStartTime(0);

		if(first != null) {
			long startTime = System.currentTimeMillis();
			setServiceStartTime(startTime);

			first = component;
			
			if(this.getId() < 2) {
				assemble();
			}
		} else {
			long startTime = System.currentTimeMillis();
			setServiceStartTime(startTime);

			second = component;
			
			assemble();
		}
		//notifyAll();
	}

	public void addBuffer(Buffer b) {
		this.assignedBuffers.add(b);
	}

	@Override
	public void run() {
		while(isRunning()) {
			if(!isOccupied()) {
				for(int i = 0; i < assignedBuffers.size(); i++) {
					if(assignedBuffers.get(i).components.peek() != null) {
						try {
							receive(assignedBuffers.get(i).pop());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				if(getOccupiedStartTime() == 0) {
					setOccupiedStartTime(System.currentTimeMillis());
				}
			}
		}
	}
}
