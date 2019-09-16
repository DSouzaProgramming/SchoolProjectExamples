package packages;

import java.util.Timer;
import java.util.TimerTask;

public class backgroundTimer extends TimerTask {
	private boolean statusFlag;
	
	Timer timer;
	TimerTask task;
	
	public backgroundTimer() {
		this.setStatus(false);
		
		
	}
	
	
	
	public boolean getStatus() {
		return this.statusFlag;
		
	}
	public void setStatus(boolean status) {
		this.statusFlag = status;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
		this.setStatus(true);
		
		
		
	}

}
