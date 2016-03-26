package main.Structures;

/**
 *
 * drawn from
 * http://www.java2s.com/Code/Java/Collections-Data-Structure/AnimplementationofthejavautilMapinterfacewhichcanonlyholdasingleobject.htm
 * Simple Data structure that relates one string to specific value
 */
public final class MicroMap<K, V> implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    public static final int MAX_ENTRIES = 1;
    private K key;
    private V value;

    public MicroMap(){}
    public MicroMap(final K key, final V value) { put(key, value); }

    public boolean isFull() {return size() == MAX_ENTRIES;}

    public int size() {return (key != null) ? 1 : 0;}

    public boolean isEmpty() {return size() == 0;}

    public boolean containsKey(final Object key) {return key.equals(this.key);}

    public boolean containsValue(final Object value) {return value.equals(this.value);}

    public K getKey(){
        return key;
    }

    public V getVal() {
        return value;
    }
    public V get(final Object key) {
        if (key.equals(this.key))
            return value;
        return null;
    }
    public V put(final K key, final V value) {
        if (key.equals(this.key)){ // Replace?
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        else {
            if (size() < MAX_ENTRIES){   // Is there room for a new entry?
                this.key = key;
                this.value = value;
                return null;
            }
            else {
                throw new IllegalStateException("Map full");
            }
        }
    }
    public V remove(final Object key) {
        if (key.equals(this.key)) {
            final V oldValue = value;
            this.key = null;
            value = null;
            return oldValue;
        }
        return null;
    }
    public void clear() {
        key = null;
        value = null;
    }
}
