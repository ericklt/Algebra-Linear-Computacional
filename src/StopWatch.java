import java.util.Calendar;
import java.util.GregorianCalendar;

public class StopWatch {

	private static final Calendar calendar = new GregorianCalendar();
	private long currentTime = 0;
	private long startTime = 0;
	
	public void start() {
		startTime = calendar.getTimeInMillis();
	}
	
	public long stop() {
		currentTime += calendar.getTimeInMillis() - startTime;
		return currentTime;
	}
	
	public long getTime() {
		return currentTime;
	}
	
	public void reset() {
		currentTime = 0;
	}
	
}
