package main.Entities;

import main.Structures.MicroMap;
import org.eclipse.jetty.util.PathWatcher;
import org.eclipse.jetty.websocket.common.io.http.HttpResponseHeaderParser;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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

    public void delete() {
        delete( m_filepath );
    }

    public static boolean delete( Path path_to_delete ) {    // taken from http://stackoverflow.com/questions/7768071/how-to-delete-directory-content-in-java
        try {
            Files.walkFileTree( path_to_delete, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException
                {
                    // System.out.println( "Deleting file: " + file );
                    Files.delete( file );
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException e)
                    throws IOException
                {
                    if ( e == null ) {
                        // System.out.println( "Deleting directory: " + dir );
                        Files.delete( dir );
                        return FileVisitResult.CONTINUE;
                    }
                    else {
                        // directory iteration failed
                        throw e;
                    }
                }
             });
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
