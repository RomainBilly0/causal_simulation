package src.causal;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Node extends Thread {
    private final int id;
    private final Deque<Message> buffer = new ConcurrentLinkedDeque<>();
    private final Map<Integer, Integer> vectorClock = new HashMap<>();
    private final Network network;
    private final Random random = new Random();
    private final NodeGraphic graphic;
    private final List<Message> view = new ArrayList<>();
    private final float replyProbability;
    private final int sendProbability;

    public Node(int id, Network network, NodeGraphic graphic, int nodeCount) {
        this.id = id;
        this.network = network;
        this.graphic = graphic;
        this.replyProbability = 0.5f / nodeCount;
        this.sendProbability = 5000 * nodeCount;
    }

    public synchronized void receive(Message message) {
        buffer.add(message);
    }

    private synchronized boolean isDeliverable(Message message) {
        Map<Integer, Integer> msgClock = message.getVectorClock();
        for (Map.Entry<Integer, Integer> entry : msgClock.entrySet()) {
            int k = entry.getKey();
            int val = entry.getValue();
            int localVal = vectorClock.getOrDefault(k, 0);
            if (k == message.getSenderId()) {
                if (val != localVal + 1) return false;
            } else {
                if (val > localVal) return false;
            }
        }
        return true;
    }

    public synchronized void deliver(Message message) {
        Map<Integer, Integer> receivedClock = message.getVectorClock();

        for (Map.Entry<Integer, Integer> entry : receivedClock.entrySet()) {
            int key = entry.getKey();
            int receivedVal = entry.getValue();
            int currentVal = vectorClock.getOrDefault(key, 0);
            vectorClock.put(key, Math.max(receivedVal, currentVal));
        }

        view.add(message);
        graphic.updateClock(new HashMap<>(vectorClock), this.buffer);
        notifyViewUpdated();

        if (message.getSenderId() != this.id && new Random().nextFloat() < replyProbability) {
            send("Reply to: " + message.getContent());
        }
    }


    public synchronized void send(String content) {
        vectorClock.put(id, vectorClock.getOrDefault(id, 0) + 1);
        Message message = new Message(id, content, new HashMap<>(vectorClock));
        graphic.updateClock(new HashMap<>(vectorClock), this.buffer);
        deliver(message);
        network.broadcast(message);
    }

    @Override
    public void run() {
        long lastMessageTime = System.currentTimeMillis();
        int interval = random.nextInt(sendProbability);

        while (true) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastMessageTime > interval && random.nextInt(100) < 20) {
                interval = random.nextInt(sendProbability);
                send("Broadcast from Node " + id);
                lastMessageTime = currentTime;
            }

            synchronized (this) {
                Iterator<Message> it = buffer.iterator();
                while (it.hasNext()) {
                    Message message = it.next();
                    if (isDeliverable(message)) {
                        it.remove();
                        deliver(message);
                    }
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public int getID() {
        return id;
    }

    public List<Message> getView() {
        return view;
    }

    public interface NodeViewListener {
        void onViewUpdated(Node node);
    }

    private final List<NodeViewListener> listeners = new ArrayList<>();

    public synchronized void addViewListener(NodeViewListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeViewListener(NodeViewListener listener) {
        listeners.remove(listener);
    }

    private synchronized void notifyViewUpdated() {
        for (NodeViewListener listener : listeners) {
            listener.onViewUpdated(this);
        }
    }
}
