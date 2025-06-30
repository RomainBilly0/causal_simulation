package src.causal;

import java.awt.*;

public class MessageDot {
    public final int senderId;
    public final int receiverId;
    public final long startTime;
    public final long duration; // in ms
    public final Color color;

    public MessageDot(int senderId, int receiverId, long delayMs, Color color) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.startTime = System.currentTimeMillis();
        this.duration = delayMs;
        this.color = color;
    }

    public float getProgress(long now) {
        return Math.min(1f, (now - startTime) / (float) duration);
    }

    public boolean isComplete(long now) {
        return getProgress(now) >= 1f;
    }
}

