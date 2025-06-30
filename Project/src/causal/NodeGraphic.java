package src.causal;

import java.awt.*;
import java.util.Deque;
import java.util.Map;

public class NodeGraphic {
    public final int id;
    public int x, y;
    public Map<Integer, Integer> vectorClock;
    public Deque<Message> nodeBuffer;

    public NodeGraphic(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillOval(x - 25, y - 25, 50, 50);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("N" + id, x - 10, y + 5);

//        if (vectorClock != null) {
//            g.setFont(new Font("Arial", Font.PLAIN, 10));
//            FontMetrics fm = g.getFontMetrics();
//
//            String vcStr = vectorClock.toString();
//            int vcWidth = fm.stringWidth(vcStr);
//            g.drawString(vcStr, x - vcWidth / 2, y + 35);
//
//            String bufferInfo = nodeBuffer.size() + " messages";
//            int msgWidth = fm.stringWidth(bufferInfo);
//            g.drawString(bufferInfo, x - msgWidth / 2, y + 45);
//        }
    }

    public void updateClock(Map<Integer, Integer> vc, Deque<Message> buffer) {
        this.vectorClock = vc;
        this.nodeBuffer = buffer;
    }
}

