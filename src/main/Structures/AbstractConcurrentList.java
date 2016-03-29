package main.Structures;

import main.Entities.DirectoryMaker;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractConcurrentList<T extends DirectoryMaker> {
    public class Node<T extends DirectoryMaker> {
        public T value;
        public Node next = null;

        public Node(T value ) {
            this.value = value;
        }
        public T getVal(){
            return value;
        }
    }

    protected Node m_head = null;
    protected Node m_tail = null;
    public AtomicInteger m_listsize = new AtomicInteger();

    public <T extends DirectoryMaker> T get(int ID) {
        Node curr = m_head.next;
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
    public abstract <T extends DirectoryMaker> T Remove( int dID );
    public abstract boolean Contains( T newValue );
    public abstract T Get( int id );
    public boolean IsEmpty() {
        return m_head == null;
    }
}
