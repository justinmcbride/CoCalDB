package main;

import main.Entities.File;
import main.Entities.FileHelper;
import main.Entities.ReferenceList;
import main.Structures.MicroMap;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Justin
 */
public abstract class Collection {
  protected Map<String, ReferenceList> m_references = new HashMap<>() ;
  protected Map<String, File<?>> m_attributes = new HashMap<>();
  protected Integer m_id = null;
  protected java.nio.file.Path m_filepath;
  public ReentrantLock m_lock = new ReentrantLock(true);

  public Integer GetID() {
    return m_id;
  }

  public void edit(List<MicroMap<String, String>> edit) {
    try {
      lock();
      Iterator<MicroMap<String, String>> itr = edit.iterator();
      while (itr.hasNext()) {
        MicroMap<String, String> change = itr.next();
        String attribute = change.getKey();
        try {m_attributes.get(attribute).SetValue(change.getVal());}
        catch (ParseException e)
        {
          System.out.println(e);
        }
      }
    }
    finally{
      unlock();
    }
  }

  public void add(MicroMap<String, List<String>> newRefs) {
    try {
      lock();
      m_references.get(newRefs.getKey()).addRefs(newRefs.getVal());
    }
    finally{
      unlock();
    }
  }

  public void remove(MicroMap<String, List<String>> remRefs) {
    try {
      lock();
      m_references.get(remRefs.getKey()).remRefs(remRefs.getVal());
    }
    finally{
      unlock();
    }
  }


  public void refresh() {
    Iterator itr = m_attributes.entrySet().iterator();
    while(itr.hasNext()) {
      File file = (File) itr.next();
      file.Refresh();
    }

    itr = m_references.entrySet().iterator();
    while(itr.hasNext()) {
      ReferenceList ref = (ReferenceList) itr.next();
      ref.Refresh();
    }
  }

  public void delete() {
    FileHelper.Delete( m_filepath );
  }

  public void lock() { m_lock.lock(); }
  public void unlock() { m_lock.unlock(); }
}
