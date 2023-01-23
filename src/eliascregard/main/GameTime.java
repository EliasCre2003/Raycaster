package eliascregard.main;

public class GameTime {

    public static final int NANO_TIME_CONVERTER_CONSTANT = 1_000_000_000;

    long lastTime;
    long currentTime;

    public GameTime() {
        this.lastTime = System.nanoTime();
        this.currentTime = lastTime;
    }

    public long getLastTime() {
        return lastTime;
    }
    public long getCurrentTime() {
        this.currentTime = System.nanoTime();
        this.lastTime = currentTime;
        return currentTime;
    }

    public double getNanoDeltaTime() {
        this.currentTime = System.nanoTime();
        long deltaT = currentTime - lastTime;
        this.lastTime = currentTime;
        return deltaT;
    }
    public double getDeltaTime() {
        return this.getNanoDeltaTime() / NANO_TIME_CONVERTER_CONSTANT;
    }

    public int getFPS(double deltaTime) {
        if (deltaTime > 0) {
            return (int) (1 / deltaTime);
        }
        return 0;
    }

    public static double secondsToNanoSeconds(double seconds) {
        return seconds * NANO_TIME_CONVERTER_CONSTANT;
    }

    public static double nanoSecondsToSeconds(double nanoSeconds) {
        return nanoSeconds / NANO_TIME_CONVERTER_CONSTANT;
    }

    public static double secondsToMilliSeconds(double seconds) {
        return seconds * 1000;
    }

    public static double milliSecondsToSeconds(double milliSeconds) {
        return milliSeconds / 1000;
    }

    public static double milliSecondsToNanoSeconds(double milliSeconds) {
        return milliSeconds * 1_000_000;
    }

    public static double nanoSecondsToMilliSeconds(double nanoSeconds) {
        return nanoSeconds / 1_000_000;
    }


}