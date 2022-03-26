package tracker.history;

public class Node<T> { // класс узла менеджера историй
    protected T data;
    protected Node<T> next;
    protected Node<T> previous;

    public Node(T data, Node<T> next, Node<T> previous) {
        this.data = data;
        this.next = next;
        this.previous = previous;
    }


}
