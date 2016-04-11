package main.Structures;

import main.Collection;

import main.Exceptions.NoSuchElementException;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeList<T extends Collection> extends AbstractConcurrentList<T> {
    private Node m_tail_pred;


    public LockFreeList() {
        super(false);
        m_head.m_next = new AtomicMarkableReference(m_tail, false);
        m_tail.m_next = new AtomicMarkableReference(null, false);
    }

    public boolean Add(T newValue) {
        boolean splice;
        while(true){
            Window window = find(newValue.GetID());
            Node pred = window.pred; Node curr = window.curr;
            if (curr.getID() == newValue.GetID()) {
                return false;
            } else {
                Node newNode = new Node(newValue);
                newNode.m_next = new AtomicMarkableReference<>(curr, false);
                if (pred.m_next.compareAndSet(curr, newNode, false, false)) {
                    m_listsize.incrementAndGet();
                    return true;
                }
            }
        }

    }

    public T Remove(Integer dID) throws NoSuchElementException {
        Boolean snip;
        while (true){
            Window window = find(dID);
            if (window == null) { return null; }
            Node pred = window.pred; Node curr = window.curr;
            if ( !curr.getID().equals(dID) ) {
                throw new NoSuchElementException(this);
            } else {
                Node succ = (Node) curr.m_next.getReference();
                snip = curr.m_next.compareAndSet(succ, succ, false, true);
                if (!snip)
                    continue;
                pred.m_next.compareAndSet(curr,succ,false,false);
                m_listsize.decrementAndGet();
                return (T) curr.getVal();
            }

        }
    }

    @Override
    public <T extends Collection> T get(Integer ID) throws NoSuchElementException {
        Node curr = (Node) m_head.m_next.getReference();
        if (curr.m_next.getReference() == null || curr == null) { return null; }
        while (curr.getVal().GetID() < ID){
            if ( curr.m_next.getReference() != m_tail) {
                curr = (Node) curr.m_next.getReference();
            } else {
                return null;
            }
        }
        //System.out.println("compare " + curr.getID() + " == " + ID + " = " + curr.getID().equals(ID));
        if (curr.getID().equals(ID)) {
            return (T) curr.getVal();
        } else {
            throw new NoSuchElementException(this);
        }
    }

    protected Window find(Integer key){ return find(m_head, key); }

    @Override
    protected Window find(Node head, Integer key){
        Node curr = (Node) head.m_next.getReference();
        if (curr == null) { return null; } // list would have to be broken
        Node pred = head;
        while(curr.getID() < key) {
            pred = curr;
            if(curr.m_next.getReference() != null) { //curr.next != tail
                curr = (Node) curr.m_next.getReference();
            }
            else { return null; }
        }
        return new Window(pred, curr);
    }

    public String toString(){
        Node curr = m_head;
        String ret = "Calendar len " + m_listsize + ": ";
        while(curr != null){
            if ( curr.getVal() != null) ret = ret + curr.getID() + ", ";
            curr = (Node) curr.m_next.getReference();
        }
        return ret;
    }

    public boolean Contains(T value) {
        return false;
    }

    public boolean isEmpty() { return m_head.m_next.getReference() == m_tail; }



    public boolean Edit(T value){
        return false;
        }
}
