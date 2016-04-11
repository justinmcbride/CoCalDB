package main.Structures;

import main.Collection;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractConcurrentList<T> {
    public class Node<T extends Collection> {
        private T value;
        public Node next;                     //only used in Lazy
        private volatile boolean m_mark; //only used in Lazy
        private ReentrantLock m_lock;       //only used in Lazy
        AtomicMarkableReference<Node> m_next; //only used in LockFree

        public Node(T value ) {
            this.value = value;
            this.next = null;
        }
        public Node(T value, boolean lock ) {
            this(value);
            if ( lock ) {
                m_lock = new ReentrantLock();
                m_mark = false;
            }
        }
        public T getVal(){
            return value;
        }
        public Integer getID(){
            return value.GetID();
        }

        public void lock() {
            m_lock.lock();
        }
        public void unlock() { m_lock.unlock(); }
        public boolean isMarked(){ return m_mark; }

        public void mark(boolean change){ m_mark = change; }

        //public Object getMark(boolean[] marked){ return m_next.get(marked); }
    }

    public class deadCol extends Collection {
        deadCol(Integer id){ m_id = id; }
    }

    Node m_head;
    Node m_tail;
    AtomicInteger m_listsize;

    AbstractConcurrentList(boolean lock) {
        m_head = new Node(new deadCol(Integer.MIN_VALUE), lock);
        m_tail = new Node(new deadCol(Integer.MAX_VALUE), lock);
        m_listsize = new AtomicInteger();
    }

    protected class Window { // taken from CSCI 4830 lecture 13
        public Node pred;
        public Node curr;
        Window(Node pred, Node curr) {
            this.pred = pred; this.curr = curr;
        }
    }

    Window find(Integer key){ return find(m_head, key);}

    Window find(Node head, Integer key){
        Node curr = head.next;
        if (curr == null) { return null; } // list would have to be broken
        Node pred = head;
        while(curr.getID() < key) {
            pred = curr;
            if(curr.next != null) { //curr.next != tail
                curr = curr.next;
            }
            else { return null; }
        }
        return new Window(pred, curr);
    }



    public <T extends Collection> T get(Integer ID) throws NoSuchElementException {
        Node curr = m_head.next;
        if (curr == m_tail || curr == null) { return null; }
        while (curr.value.GetID() < ID) {
            if (curr.next != m_tail) {
                curr = curr.next;
            } else {
                return null;
            }
        }
        if (curr.value.GetID().equals(ID))
            return (T) curr.getVal();
        else
            throw new main.Exceptions.NoSuchElementException(this);
    }

    public abstract boolean Add( T newValue );
    public abstract T Remove(Integer dID ) throws NoSuchElementException;
    public abstract boolean Contains( T newValue );
    public abstract boolean isEmpty();
}
