package main.Structures;

import main.Entities.DirectoryMaker;

public class LazyList<T extends DirectoryMaker> extends AbstractConcurrentList<T> {
    private Node m_tail_pred;

    public LazyList() {
        super();
        m_head = new Node(null, true);
        m_tail = new Node(null, true);
        m_head.next = m_tail;
        m_tail_pred = m_head;
    }

    private Boolean Validate(Node pred, Node curr ) {
        return !pred.m_next.isMarked() && !curr.m_next.isMarked() && (pred.next == curr);
    }

    public boolean Add(T newValue) {
        if (newValue == null) {return false;}
        Node<T> newNode = new Node<>( newValue, true );
        Node pred = m_tail_pred;
        try {
            pred.lock();
            m_tail.lock();
            if (Validate(pred, m_tail)) {
                pred.next = newNode;
                m_tail_pred = newNode;
                newNode.next = m_tail;
                m_listsize.incrementAndGet();
                return true;
            } else {
                Thread.yield();
                return Add(newValue);
            }
        }
        finally {
            pred.unlock();
            m_tail.unlock();
        }

    }

    public boolean isEmpty(){
        return m_tail == m_head;
    }

    public <T extends DirectoryMaker> T Remove(int dID) {
        Window window = find(dID);
        if( window == null ) return null;
        Node pred = window.pred;
        Node curr = window.curr;
        try {
            pred.lock();
            curr.lock();
            if (Validate(pred, curr)) {
                curr.m_next.set(curr, true);
                pred.next = curr.next;
                m_listsize.decrementAndGet();
                return (T) curr.value;
            } else {
                Thread.yield();
                return Remove(dID);
            }
        }
        finally {
            pred.unlock();
            curr.unlock();
        }
    }

    public String toString(){
        Node curr = m_head.next;
        String ret = "Calendar len " + m_listsize + ": ";
        while(curr != null){
            if ( curr.getVal() != null) ret = ret + curr.getVal().m_ID + ", ";
            curr = curr.next;
        }
        return ret;
    }

    public boolean Contains(T value) {
        return false;
    }




    public boolean Edit(T value){
        return false;
        }
}
