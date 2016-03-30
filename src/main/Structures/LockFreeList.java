package main.Structures;

import main.Entities.DirectoryMaker;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeList<T extends DirectoryMaker> extends AbstractConcurrentList<T> {
    private Node m_tail_pred;


    public LockFreeList() {
        super();
        m_head = new Node(null);
        m_tail = new Node(null);
        m_head.m_next = new AtomicMarkableReference(m_tail, false);
        m_tail.m_next = new AtomicMarkableReference(null, false);
        m_head.next = m_tail;
        m_tail_pred = m_head;
    }

    public boolean Add(T newValue) {
        boolean splice;
        while(true){
            Node pred = m_tail_pred; Node curr = m_tail;
            Node node = new Node(newValue);
            node.m_next = new AtomicMarkableReference<>(curr, false);
            if (pred.m_next.compareAndSet(curr, node, false, false)) {
                node.next = curr;
                pred.next = node;
                m_tail_pred = node;
                m_listsize.incrementAndGet();
                return true; }
        }

    }

    public boolean isEmpty(){
        return m_tail == m_head;
    }

    public <T extends DirectoryMaker> T Remove(int dID) {
        Boolean snip;
        while (true){
            Window window = find(dID);
            if (window == null) { return null; }
            Node pred = window.pred; Node curr = window.curr;
            Node succ = (Node) curr.m_next.getReference();
            snip = curr.m_next.compareAndSet(succ, succ, false, true);
            if (!snip) continue;
            pred.m_next.compareAndSet(curr,succ,false,false);
            pred.next = succ;
            m_listsize.decrementAndGet();
            return (T) curr.getVal();
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
