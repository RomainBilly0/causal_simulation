package src.causal;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Network {
    private final List<Node> nodes;
    private final Random random = new Random();
    private final GraphPanel graphPanel;

    public Network(List<Node> nodes, GraphPanel graphPanel) {
        this.nodes = nodes;
        this.graphPanel = graphPanel;
    }

    public void broadcast(Message message) {
        int senderId = message.getSenderId();
        Color dotColor = Color.getHSBColor(random.nextFloat(), 0.6f, 1.0f);

        for (Node node : nodes) {
            if (node.getID() == senderId) continue;

            int delay = 2000 + random.nextInt(5000);

            MessageDot dot = new MessageDot(senderId, node.getID(), delay, dotColor);
            graphPanel.addMessageDot(dot);

            new Thread(() -> {
                try {
                    Thread.sleep(delay);
                    node.receive(message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
