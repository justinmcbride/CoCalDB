package main;

import main.Entities.File;
import main.Entities.FileHelper;
import main.Entities.ReferenceFile;
import main.Entities.ReferenceList;
import main.Structures.MicroMap;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Justin
 */
public abstract class Collection {
  Map<String, ReferenceList> m_references = new TreeMap<>() ;
  Map<String, File<?>> m_attributes = new TreeMap<>();
  protected Integer m_id = null;
  java.nio.file.Path m_filepath;
  private ReentrantLock m_lock = new ReentrantLock(true);

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

  public void link(MicroMap<String, List<String>> newRefs) {
    try {
      lock();
      m_references.get(newRefs.getKey()).addRefs(newRefs.getVal());
    }
    finally{
      unlock();
    }
  }

  public void unlink(MicroMap<String, List<String>> remRefs) {
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


  public String toString(){
    String ret = "";
    Iterator<Map.Entry<String, File<?>>> itrAtt = m_attributes.entrySet().iterator();
    while (itrAtt.hasNext()) {
      Map.Entry<String, File<?>> pair = itrAtt.next();
      String name = pair.getKey();
      String value = pair.getValue().toString();
      ret += name + ": " + value + ", ";
    }
    Iterator<Map.Entry<String, ReferenceList>> itrRef = m_references.entrySet().iterator();
    while (itrRef.hasNext()) {
      Map.Entry<String, ReferenceList> pair = itrRef.next();
      String name = pair.getKey();
      ReferenceList refs = pair.getValue();
      ret += name + ": " + refs.toString();
    }

    return ret;
  }

  public void delete() {
    FileHelper.Delete( m_filepath );
  }
  private void lock() { m_lock.lock(); }
  private void unlock() { m_lock.unlock(); }
}
