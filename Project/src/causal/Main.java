package src.causal;

import javax.swing.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int n = 100;
        List<NodeGraphic> graphics = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = (int) (600 + 350 * Math.cos(angle));
            int y = (int) (400 + 350 * Math.sin(angle));
            graphics.add(new NodeGraphic(i, x, y));
        }

        GraphPanel graphPanel = new GraphPanel(graphics);
        Network network = new Network(nodes, graphPanel);
        for (int i = 0; i < n; i++) {
            Node node = new Node(i, network, graphics.get(i), n);
            nodes.add(node);
        }

        SwingUtilities.invokeLater(() -> new GraphFrame(graphics, nodes, graphPanel));

        for (Node node : nodes) {
            node.start();
        }
        nodes.getFirst().send(">$ Hello World!");
    }
}


