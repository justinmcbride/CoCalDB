package main.Entities;

import main.Structures.MicroMap;
import org.eclipse.jetty.util.PathWatcher;
import org.eclipse.jetty.websocket.common.io.http.HttpResponseHeaderParser;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract class so that CoCal classes can create directories
 */
public abstract class DirectoryMaker {
    public ReentrantLock lock = new ReentrantLock(true);
    public int m_ID;
    protected java.nio.file.Path m_filepath;
    protected Map<String, ReferenceList> m_references = new HashMap<>() ;
    protected Map<String, File<?>> m_attributes = new HashMap<>();

    protected Integer m_id = null;

    public Integer GetID() {
        return m_id;
    }

    protected Boolean CreateDirectory( Path path )
    {
        try {
            Files.createDirectories( path );
        }
        catch( FileAlreadyExistsException e ) {
            System.out.println( "\tdirectory already exists: " + e.getMessage() );
            return false;
        }
        catch( IOException e ) {
            System.out.println( "\tError creating directory: " + e.getMessage() );
            return false;
        }
        return true;
    }

    public void edit(List<MicroMap<String, String>> edit){
        try {
            lock.lock();
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
            lock.unlock();
        }
    }

    public void refresh(){
        Iterator itr = m_attributes.entrySet().iterator();
        while(itr.hasNext()){
            File file = (File) itr.next();
            file.Refresh();
        }
        itr = m_references.entrySet().iterator();
        while(itr.hasNext()){
            ReferenceList ref = (ReferenceList) itr.next();
            ref.refresh();
        }
    }
    public void delete(){
        deleteFolder(m_filepath.toFile());
    }
    public static void deleteFolder(java.io.File folder) {    // taken from http://stackoverflow.com/questions/7768071/how-to-delete-directory-content-in-java
        java.io.File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(java.io.File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    System.out.println("deleting " + f.toString());
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
