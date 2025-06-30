package src.causal;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphFrame extends JFrame {
    private final GraphPanel graphPanel;
    private final JTextPane chatPane = new JTextPane();
    private int currentChatNodeId = -1;
    private Map<Integer, NodeGraphic> nodeGraphicMap;
    private final MenuBar menuBar;
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private boolean chatViewActive = false;
    private JScrollPane chatScrollPane;

    public GraphFrame(List<NodeGraphic> nodes, List<Node> nodesList, GraphPanel graphPanel) {
        setTitle("Causal Broadcast Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);

        this.graphPanel = graphPanel;
        chatPane.setEditable(false);
        this.menuBar = new MenuBar(this, nodes);

        nodeGraphicMap = new HashMap<>();
        for (NodeGraphic ng : nodes) {
            nodeGraphicMap.put(ng.id, ng);
        }

        for (Node node : nodesList) {
            nodeMap.put(node.getID(), node);

            node.addViewListener(updatedNode -> {
                if (chatViewActive && updatedNode.getID() == currentChatNodeId) {
                    SwingUtilities.invokeLater(() ->
                            showChatViewForNode(nodeGraphicMap.get(updatedNode.getID()))
                    );
                }
            });
        }

        setJMenuBar(menuBar);
        setContentPane(graphPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showGraphView() {
        chatViewActive = false;
        setContentPane(graphPanel);
        revalidate();
        repaint();
    }

    public void showChatViewForNode(NodeGraphic nodeGraphic) {
        chatViewActive = true;
        currentChatNodeId = nodeGraphic.id;
        Node node = nodeMap.get(nodeGraphic.id);

        if (chatScrollPane == null) {
            chatScrollPane = new JScrollPane(chatPane);
        }
        if (getContentPane() != chatScrollPane) {
            setContentPane(chatScrollPane);
            revalidate();
            repaint();
        }

        JScrollBar verticalBar = chatScrollPane.getVerticalScrollBar();
        int scrollPos = verticalBar.getValue();
        int maxScroll = verticalBar.getMaximum() - verticalBar.getVisibleAmount();

        boolean isAtBottom = (maxScroll - scrollPos) <= 20;

        chatPane.setText("");
        StyledDocument doc = chatPane.getStyledDocument();

        Style defaultStyle = chatPane.addStyle("Default", null);
        StyleConstants.setFontSize(defaultStyle, 14);
        StyleConstants.setFontFamily(defaultStyle, "Monospaced");

        Style meStyle = chatPane.addStyle("Me", null);
        StyleConstants.setForeground(meStyle, new Color(30, 144, 255));
        StyleConstants.setBold(meStyle, true);
        StyleConstants.setFontSize(meStyle, 14);
        StyleConstants.setFontFamily(meStyle, "Monospaced");

        Style senderStyle = chatPane.addStyle("Sender", null);
        StyleConstants.setForeground(senderStyle, Color.BLACK);
        StyleConstants.setBold(senderStyle, true);
        StyleConstants.setFontSize(senderStyle, 14);
        StyleConstants.setFontFamily(senderStyle, "Monospaced");

        Style titleStyle = chatPane.addStyle("Title", null);
        StyleConstants.setFontSize(titleStyle, 16);
        StyleConstants.setBold(titleStyle, true);
        StyleConstants.setAlignment(titleStyle, StyleConstants.ALIGN_CENTER);

        try {
            doc.insertString(doc.getLength(), "Chat history for Node " + node.getID() + "\n\n", titleStyle);
            for (Message message : node.getView()) {
                boolean isMe = message.getSenderId() == node.getID();
                String senderLine = (isMe ? "*ME*" : "*Node " + message.getSenderId() + "*") + "\n";
                String contentLine = message.getContent() + "\n\n";

                doc.insertString(doc.getLength(), senderLine, isMe ? meStyle : senderStyle);
                doc.insertString(doc.getLength(), contentLine, defaultStyle);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JScrollBar vBar = chatScrollPane.getVerticalScrollBar();
            if (isAtBottom) {
                vBar.setValue(vBar.getMaximum());
            } else {
                vBar.setValue(Math.min(scrollPos, vBar.getMaximum()));
            }
        });
    }

}
