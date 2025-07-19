package src.private_TO;

public class Message {
    private final int senderId;
    private final String content;
    private final int receiverId;
    private final int timestamp;

    public Message(int senderId, String content, int receiverId) {
        this.senderId = senderId;
        this.content = content;
        this.receiverId = receiverId;
        this.timestamp = (int) (System.currentTimeMillis() / 1000);
    }

    public int getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[From Node " + senderId + " | " + content + " | To Node " + receiverId + " | TS: " + timestamp + "]";
    }
}
