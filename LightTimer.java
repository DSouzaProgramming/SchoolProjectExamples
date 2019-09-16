package packages;
import java.util.Timer;
import java.util.TimerTask;

public class LightTimer extends TimerTask {
	
	static int timeout = 4000;
	static Timer timer;
	static TimerTask task;
	static boolean timedout =true;
	
	
	static void initTimer() {
		timer = new Timer();
		task = new LightTimer();
		timedout = false;
		timer.schedule(task, timeout);
		
	}
	static void stop() {
		timer.cancel();
		timer.purge();
		timedout = true;
		
	}
	public void run() { // runs every 5 seconds
		
		timedout = true;
		
		
		
	}
	
	
	
	

}
