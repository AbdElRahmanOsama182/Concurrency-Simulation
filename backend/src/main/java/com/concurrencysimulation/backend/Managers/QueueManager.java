package com.concurrencysimulation.backend.Managers;

import java.util.HashMap;
import java.util.Map;

import com.concurrencysimulation.backend.Models.Queue;

public class QueueManager {
    private static QueueManager instance = null;
    private int nextId = 0;
    private Map<Integer, Queue> queues = new HashMap<Integer, Queue>();

    private QueueManager() {
    }

    public static synchronized QueueManager getInstance() {
        if (instance == null) {
            instance = new QueueManager();
        }
        return instance;
    }

    public Queue createQueue() {
        Queue queue = new Queue(nextId);
        nextId++;
        queues.put(queue.getQueueId(), queue);

        return queue;
    }

    public Queue getQueue(int id) {
        if (queues.containsKey(id))
            return queues.get(id);
        return null;
    }

    public void removeQueue(int id) {
        if (queues.containsKey(id))
            queues.remove(id);
    }

    public Map<Integer, Queue> getQueues() {
        return queues;
    }

    public void clear() {
        queues.clear();
    }
}
