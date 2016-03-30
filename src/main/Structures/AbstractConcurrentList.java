package main.Structures;

import main.Entities.DirectoryMaker;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractConcurrentList<T extends DirectoryMaker> {
    public class Node<T extends DirectoryMaker> {
        public T value;
        public Node next = null;
        AtomicMarkableReference<Node> m_next;
        protected ReentrantLock m_lock = null;

        public Node(T value ) {
            this.value = value;
            m_next = new AtomicMarkableReference<>(this, false);
        }
        public Node(T value, boolean lock ) {
            this(value);
            if ( lock ) { m_lock = new ReentrantLock(); }
        }
        public T getVal(){
            return value;
        }

        public void lock() {
            m_lock.lock();
        }
        public void unlock() { m_lock.unlock(); }
        public boolean isMarked(){ return m_next.isMarked(); }
        public Object getMark(boolean[] marked){ return m_next.get(marked); }
    }

    protected Node m_head = null;
    protected Node m_tail = null;
    public AtomicInteger m_listsize = null;

    AbstractConcurrentList() {
        m_listsize = new AtomicInteger();
    }

    protected class Window { // taken from CSCI 4830 lecture 13
        public Node pred;
        public Node curr;
        Window(Node pred, Node curr) {
            this.pred = pred; this.curr = curr;
        }
    }

    protected Window find(Node<T> head, int key){
        Node curr = head.next;
        if (curr.next == null) { return null; }
        Node pred = head;
        while(curr.value.m_ID != key) {
            pred = curr;
            if(curr.next.next != null) { //curr.next != tail
                curr = curr.next;
            }
            else { return null; }
        }
        return new Window(pred, curr);
    }

    protected Window find(int key){ return find(m_head, key);}

    public <T extends DirectoryMaker> T get(int ID) {
        Node curr = m_head.next;
        if (curr == m_tail || curr == null) { return null; }
        while (curr.value.m_ID != ID) {
            if (curr.next.next != null) { //curr.next != tail
                curr = curr.next;
            } else {
                return null;
            }
        }
        return (T) curr.getVal();
    }





    public abstract boolean Add( T newValue );
    public abstract <T extends DirectoryMaker> T Remove( int dID );
    public abstract boolean Contains( T newValue );
    public boolean IsEmpty() {
        return m_head == null;
    }
}
