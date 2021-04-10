import java.util.List;
import java.lang.Thread;
import java.util.Random;

public class Inspector extends Entity {

	public Inspector(int id) {
		super(id);
	}
	
	public void inspect(Component c) throws InterruptedException {
		sleep((long) (this.getWaitTime(c.getType()) * 1000));
		int index = 0;

		if(this.getId() == 1) {
			int shortestBufferId = 0;
			int shortestBufferSize = assignedBuffers.get(0).components.size();
			for(int i = 0; i < assignedBuffers.size(); i++) {
				if (assignedBuffers.get(i).components.size() < shortestBufferSize) {
					shortestBufferId = i;
					shortestBufferSize = assignedBuffers.get(i).components.size();
				}
			}
			index = shortestBufferId;
		} else {
			for(int i = 0; i < assignedBuffers.size(); i++) {
				if(assignedBuffers.get(i).getAssignedComponent() == c.getType()) {
					//System.out.println("Inspector " + this.getId() + " passed C" + c.getType() + " to buffer.");
					index = i;
					break;
				}
			}
		}
		assignedBuffers.get(index).receive(c);

		setOccupied(false);
		Tracking.getInstance().addServiceTime("inspector"+getId()+c.getType(), (double) (System.currentTimeMillis() - getServiceStartTime()));
	}
	
	@Override
	public void receive(Component c) throws InterruptedException {
		long startTime = System.currentTimeMillis();
		setServiceStartTime(startTime);
		setOccupiedStartTime(startTime);
		while(isOccupied()) {
			try {
				wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		Tracking.getInstance().addOccupiedTime("inspector"+getId()+c.getType(), (double) (System.currentTimeMillis() - getOccupiedStartTime()));

		this.setOccupied(true);
		inspect(c);
		//notifyAll();
	}

	public void addBuffer(Buffer b) {
		assignedBuffers.add(b);
	}

	@Override
	public void run() {
		Random componentRN = new Random();
		Component c;
		while(isRunning()) {
			if(this.getId() == 1) {
				c = new Component(1);
			} else {
				int randComponentType = componentRN.nextInt(4-2) + 2;
				c = new Component(randComponentType);
			}
			try {
				this.receive(c);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
