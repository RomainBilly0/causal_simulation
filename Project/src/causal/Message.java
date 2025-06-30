package src.causal;

import java.util.Map;

public class Message {
    private final int senderId;
    private final String content;
    private final Map<Integer, Integer> vectorClock;

    public Message(int senderId, String content, Map<Integer, Integer> vectorClock) {
        this.senderId = senderId;
        this.content = content;
        this.vectorClock = vectorClock;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public Map<Integer, Integer> getVectorClock() {
        return vectorClock;
    }

    @Override
    public String toString() {
        return "[From Node " + senderId + " | " + content + " | VC: " + vectorClock + "]";
    }
}
