package src.private_TO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {
    public final Map<Integer, Node> nodeMap = new HashMap<>();

    public Network(List<Node> nodes) {
        for (Node node : nodes) {
            nodeMap.put(node.getID(), node);
        }
    }

    public void carryMessage(Message message) {

        new Thread(() -> {
            try {
                int delay = 2000 + new java.util.Random().nextInt(2000);
                int senderId = message.getSenderId();
                int receiverId = message.getReceiverId();
                Node sender = nodeMap.get(senderId);
                Node receiver = nodeMap.get(receiverId);

                Thread.sleep(delay);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
