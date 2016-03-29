package main.Structures;

import main.Entities.DirectoryMaker;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractConcurrentList<T extends DirectoryMaker> {
    public class ListNode<T extends DirectoryMaker> {
        public T value;
        public ListNode next;

        public ListNode( T value ) {
            this.value = value;
            next = null;
        }
        public T getVal(){
            return value;
        }
    }

    protected ListNode m_head;
    protected ListNode m_tail;
    public AtomicInteger m_listsize = new AtomicInteger();

    public <T extends DirectoryMaker> T get(int ID) {
        ListNode curr = m_head;
        while (curr.value.m_ID != ID) {
            if (curr != null) {
                curr = curr.next;
            } else {
                return null;
            }
        }
        return (T) curr.getVal();
    }

    AbstractConcurrentList() {
        m_head = null;
        m_tail = null;
    }

    public abstract boolean Add( T newValue );
    public abstract <T extends DirectoryMaker> T Remove( T valueToRemove );
    public abstract boolean Contains( T newValue );
    public boolean IsEmpty() {
        return m_head == null;
    }
}
