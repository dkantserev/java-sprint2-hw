package tracker.history;


import tracker.Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;
    private Map<Integer, Node<Task>> idNodeMap = new HashMap<>();

    @Override
    public int getSize() { // геттер размера листа
        return size;
    }

    @Override
    public void add(Task task) { // добавляет задачу в историю

        if (idNodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }

        final Node<Task> l = tail;
        final Node<Task> newNode = new Node<>(task, null, tail);
        tail = newNode;
        if (l == null) {
            head = newNode;
        } else {
            l.next = newNode;
        }

        idNodeMap.put(task.getId(), newNode);
        size++;

    }


    @Override
    public List<Task> getHistory() { // Возвращает статистику по запросам
        List<Task> historyList = new ArrayList<>();
        Node<Task> firstNode = head;
        while (firstNode != null) {
            historyList.add(firstNode.data);
            firstNode = firstNode.next;
        }
        return historyList;
    }

    @Override
    public void remove(Integer id) { // удаляет узел через id задачи
        if (idNodeMap.containsKey(id)) {
            removeNode(idNodeMap.get(id));
        }
    }

    private void removeNode(Node<Task> x) { // удаляет узел
        final Task element = x.data;
        final Node<Task> next = x.next;
        final Node<Task> prev = x.previous;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            x.previous = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.previous = prev;
            x.next = null;
        }

        x.data = null;
        size--;
    }

    public void clearHistory() { // удаляет всю историю
        for (Node<Task> x = head; x != null; ) {
            Node<Task> next = x.next;
            x.data = null;
            x.next = null;
            x.previous = null;
            x = next;
        }
        head = tail = null;
        size = 0;

    }


}


