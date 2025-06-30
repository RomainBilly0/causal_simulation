package src.causal;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GraphPanel extends JPanel {
    private final List<NodeGraphic> nodes;
    private final List<MessageDot> messageDots = new CopyOnWriteArrayList<>();


    public GraphPanel(List<NodeGraphic> nodes) {
        this.nodes = nodes;
        setPreferredSize(new Dimension(1200, 900));
        Timer timer = new Timer(30, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (NodeGraphic node : nodes) {
            node.draw(g);
        }

        long now = System.currentTimeMillis();
        for (MessageDot dot : messageDots) {
            NodeGraphic sender = getNodeById(dot.senderId);
            NodeGraphic receiver = getNodeById(dot.receiverId);
            if (sender == null || receiver == null) continue;

            float progress = dot.getProgress(now);
            int x = (int) (sender.x + progress * (receiver.x - sender.x));
            int y = (int) (sender.y + progress * (receiver.y - sender.y));

            g.setColor(dot.color);
            g.fillOval(x - 5, y - 5, 10, 10);
        }

        messageDots.removeIf(dot -> dot.isComplete(now));
    }

    public void addMessageDot(MessageDot dot) {
        messageDots.add(dot);
    }

    private NodeGraphic getNodeById(int id) {
        for (NodeGraphic node : nodes) {
            if (node.id == id) {
                return node;
            }
        }
        return null;
    }
}
