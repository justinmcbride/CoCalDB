package main.Exceptions;

import main.Collection;
import main.Structures.AbstractConcurrentList;

/**
 * Created by WarrenFerrell Jewell on 4/11/2016.
 */
public class NoSuchElementException extends java.util.NoSuchElementException{
    private AbstractConcurrentList list;
    public <T extends AbstractConcurrentList> NoSuchElementException(T list){
        this.list = list;
    }
    public NoSuchElementException() { super(); }

    public NoSuchElementException(String m) { super(m); }

    public void printStructure(){
        System.out.println(list);
    }
}
