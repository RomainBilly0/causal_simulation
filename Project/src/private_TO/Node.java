package src.private_TO;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node extends Thread {
    private final int id;
    private final Map<Integer, List<Message>> channelMap = new HashMap<>();
    private final Network network;

    public Node(int id, Network network) {
        this.id = id;
        this.network = network;
    }

    public void send(String content, int receiverId) {
        Message message = new Message(id, content, receiverId);
        List<Message> channel = channelMap.get(receiverId);
        if (channel == null) {
            channelMap.put(receiverId, List.of(message));
        } else {
            channelMap.get(receiverId).add(message);
        }

        network.carryMessage(message);
        System.out.println("Sent message: " + message);
        System.out.println("Channel: " + channelMap.get(receiverId));
        System.out.println("Channel size: " + channelMap.get(receiverId).size());
    }

    public void receive(Message message) {
        int receiverId = message.getReceiverId();
        List<Message> channel = channelMap.get(receiverId);

        if (channel == null) {
            channelMap.put(receiverId, List.of(message));
        } else {
            channelMap.get(receiverId).add(message);
            channelMap.get(receiverId).sort(Comparator.comparingInt(Message::getTimestamp));
        }
    }

    @Override
    public void run() {
        while (true) {

        }
    }

    public int getID() {
        return id;
    }
}
