package com.github.tetrisanalyzer.game;

public class XPerLastSeconds {
    private long startMillis;
    private long startX;
    public int xPerSecond;

    // Update x/sec more often when the game starts, end with updating every 10 sec.
    private int[] everyMillis = new int[] { 10, 50, 100, 500, 1000, 2000, 10000};
    public int idx = 0;

    public XPerLastSeconds(long startMillis, long x) {
        this.startMillis = startMillis;
        this.startX = x;
    }

    public void update(long millis, long x) {
        long duration = millis - startMillis;

        if (duration >= everyMillis[idx]) {
            xPerSecond = (int) (((x - startX) * 1000) / duration);
            startMillis = millis;
            startX = x;

            if (idx < everyMillis.length - 1) {
                idx++;
            }
        }
    }
}
