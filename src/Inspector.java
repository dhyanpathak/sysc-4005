import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Inspector extends Entity {
	List<Workstation> trackedWorkstations;

	public Inspector(int id) {
		super(id);
		trackedWorkstations = new ArrayList<Workstation>();
	}

	public List<Workstation> getTrackedWorkstations() {
		return trackedWorkstations;
	}

	public void setTrackedWorkstations(List<Workstation> trackedWorkstations) {
		this.trackedWorkstations = trackedWorkstations;
	}

	public void inspect(Component c) throws InterruptedException {
		sleep((long) (this.getWaitTime(c.getType()) * 1000));
		int index = 0;

		if(this.getId() == 1) {
			int shortestBufferId = 0;
			//ALT DESIGN
			int shortestProcessingTime = trackedWorkstations.get(0).getProcessingTime();
			for(int i = trackedWorkstations.size() - 1; i != 0; i--) {
				if(shortestProcessingTime > trackedWorkstations.get(i).getProcessingTime()) {
					shortestProcessingTime = trackedWorkstations.get(i).getProcessingTime();
					shortestBufferId = i;
				}
			}
			index = shortestBufferId;
		} else {
			for(int i = 0; i < assignedBuffers.size(); i++) {
				if(assignedBuffers.get(i).getAssignedComponent() == c.getType()) {
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
