package main.Structures;

import main.Entities.DirectoryMaker;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.ReentrantLock;

public class LazyList<T extends DirectoryMaker> extends AbstractConcurrentList<T> {

    private LazyListNode m_head;
    private LazyListNode m_tail;
    private LazyListNode m_tail_pred;

    //private ReentrantLock m_lock;

    class LazyListNode<V extends DirectoryMaker> extends ListNode<V> {
        LazyListNode next = null;
        private ReentrantLock m_lock;
        AtomicMarkableReference<LazyListNode> m_marked;

        LazyListNode( V val ) {
            super( val );
            m_lock = new ReentrantLock();
            m_marked = new AtomicMarkableReference<>(this, false);
        }
        LazyListNode(boolean init){
            this(null);
        }

        public void lock() {
            m_lock.lock();
        }

        public void unlock() {
            m_lock.unlock();
        }
    }

    public LazyList() {
        super();
        m_head = new LazyListNode(false);
        m_tail = new LazyListNode(false);
        m_head.next = m_tail;
        m_tail_pred = m_head;
    }

    private Boolean Validate( LazyListNode pred, LazyListNode curr ) {
        return !pred.m_marked.isMarked() && !curr.m_marked.isMarked() && (pred.next == curr);
    }

    @Override
    public boolean Add(T newValue) {
        if (newValue == null) {return false;}
        LazyListNode<T> newNode = new LazyListNode<>( newValue );
        LazyListNode pred = m_tail_pred;
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

    public <T extends DirectoryMaker> T Remove(T valueToRemove) {
        if (valueToRemove == null) { return null; }
        LazyListNode curr = m_head;
        LazyListNode pred = null;
        while(curr.value != valueToRemove) {
            pred = curr;
            if(curr != null) {
                curr = curr.next;
            }
            else { return null; }
        }
        try {
            pred.lock();
            curr.lock();
            if (Validate(pred, curr)) {
                curr.m_marked.set(curr, true);
                pred.next = curr.next;
                m_listsize.decrementAndGet();
                return (T) curr.value;
            } else {
                Thread.yield();
                return Remove(valueToRemove);
            }
        }
        finally {
            pred.unlock();
            curr.unlock();
        }

    }

    @Override
    public boolean Contains(T value) {
        return false;
    }




    public boolean Edit(T value){
        return false;
        }
}
