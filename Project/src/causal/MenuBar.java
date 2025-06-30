package src.causal;

import javax.swing.*;
import java.util.List;

public class MenuBar extends JMenuBar {
    public MenuBar(GraphFrame parentFrame, List<NodeGraphic> nodes) {
        JButton graphButton = new JButton("Graph View");
        graphButton.addActionListener(e -> parentFrame.showGraphView());

        JMenu chatMenu = new JMenu("Chat View");
        for (NodeGraphic node : nodes) {
            JMenuItem item = new JMenuItem("Node " + node.id);
            item.addActionListener(e -> parentFrame.showChatViewForNode(node));
            chatMenu.add(item);
        }

        add(graphButton);
        add(chatMenu);
    }
}

