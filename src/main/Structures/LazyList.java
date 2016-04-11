package main.Structures;

import main.Collection;

import main.Exceptions.NoSuchElementException;


public class LazyList<T extends Collection> extends AbstractConcurrentList<T> {

    public LazyList() {
        super(true);
        m_head.next = m_tail;
    }

    private Boolean Validate(Node pred, Node curr) {
        return !pred.isMarked() && !curr.isMarked() && (pred.next == curr);
    }

    public boolean Add(T newValue) {
        if (newValue == null) { return false; }
        Node newNode = new Node<>(newValue, true);
        while (true) {
            Window window = find(newValue.GetID());
            Node pred = window.pred; Node curr = window.curr;
            if (curr.getID() == newValue.GetID()) {
                return false;
            }
            try {
                pred.lock();
                curr.lock();
                if (Validate(pred, curr)) {
                    pred.next = newNode;
                    newNode.next = curr;
                    m_listsize.incrementAndGet();
                    return true; // thread will Always add
                } else {
                    Thread.yield();
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    public T Remove(Integer dID) throws NoSuchElementException {
        if (dID < 0) { throw new NoSuchElementException("IDs cannot be negative"); }
        while (true) {
            Window window = find(dID);
            if (window == null) { return null; }
            Node pred = window.pred;
            Node curr = window.curr;
            if (!(curr.getID().equals(dID))) { throw new NoSuchElementException(this); }
            try {
                pred.lock();
                curr.lock();
                if (Validate(pred, curr)) {
                    curr.mark(true);
                    pred.next = curr.next;
                    m_listsize.decrementAndGet();
                    return (T) curr.getVal();
                } else {
                    Thread.yield();
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    public boolean isEmpty() {
        return m_head.next == m_tail;
    }

    public boolean Contains(T value) {
        return Contains(value.GetID());
    }

    private boolean Contains(Integer id) {
        Node curr = find(id).curr;
        return (curr.getID() == id);
    }

    public boolean Edit(T value) {
        return false;
    }

    @Override
    public String toString() {
        Node curr = m_head;
        String ret = "Calendar len " + m_listsize + ": ";
        while (curr != null) {
            if (curr.getVal() != null) ret = ret + curr.getID() + ", ";
            curr = curr.next;
        }
        return ret;
    }
}