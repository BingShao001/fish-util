package com.yb.fish.delay;



import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
* 补偿的延时任务
* @author bing
* @create 2018/3/12
* @version 1.0
**/
class EventOffsetDelay<T> implements Delayed {

	public static final int DELAY_TIME = 3000;
    public static final int PEEK = -1;
    public static final int EQUAL = 0;
    public static final int LAST =  1;
    private T delayData;
	private long startTime;

	public EventOffsetDelay(long startTime, T delayData) {
		this.delayData = delayData;
		this.startTime = startTime;
	}

    /**
     * get delayTime
     * @param unit
     * @return
     */
	@Override
	public long getDelay(TimeUnit unit) {
		return this.startTime+ DELAY_TIME - System.currentTimeMillis();
	}

    /**
     *
     * @param o
     * @return
     */
	@Override
	public int compareTo(Delayed o) {
		EventOffsetDelay that = (EventOffsetDelay) o;
		if (this.startTime > that.startTime) {
			return LAST;
		} else if (this.startTime == that.startTime) {
			return EQUAL;
		} else {
			return PEEK;
		}

	}

    public T getDelayData() {
        return delayData;
    }

    public void setDelayData(T delayData) {
        this.delayData = delayData;
    }
}