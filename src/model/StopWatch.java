package model;

public class StopWatch {

	private long currentTime = 0;
	private long startTime = 0;
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public long stop() {
		currentTime += System.currentTimeMillis() - startTime;
		return currentTime;
	}
	
	public long getTime() {
		return currentTime;
	}
	
	public void reset() {
		currentTime = 0;
	}
	
}
