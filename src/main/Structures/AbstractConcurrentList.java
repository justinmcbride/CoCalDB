package main.Structures;

public abstract class AbstractConcurrentList<T> {
    class ListNode<T> {
        public T value;
        public ListNode next;

        public ListNode( T value ) {
            this.value = value;
            next = null;
        }
    }

    protected ListNode m_head;
    protected ListNode m_tail;

    AbstractConcurrentList() {
        m_head = null;
        m_tail = null;
    }

    public abstract boolean Add( T newValue );
    public abstract boolean Remove( T valueToRemove );
    public abstract boolean Contains( T newValue );
    public boolean IsEmpty() {
        return m_head == null;
    }
}
