package main.Entities;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Object to hold list of references to other db collection documents
 */
public class ReferenceList {
    private Boolean m_single;
    private ArrayList<ReferenceFile> m_list;
    private Path m_path;

    public ReferenceList( Path myPath, boolean exists ) {
        m_path = myPath;
        m_list = new ArrayList<>();
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream( m_path );
            for( Path item : stream ) {
                m_list.add( new ReferenceFile( item, true ) );
            }
        }
        catch( IOException e ) {
            System.out.println( "Error creating reference file: " + e );
        }
    }

    public ReferenceList( Path myPath ) {
        m_path = myPath;
        m_list = new ArrayList<>();
        FileHelper.CreateDirectory( m_path );
    }

    public ReferenceList( String id, Path myPath ) {
        this(myPath);
        m_single = true;
        CreateFile( id );
    }

    public ReferenceList( ArrayList<String> ids, Path myPath ) {
        this(myPath);
        m_single = false;
        for( String id : ids ) {
            CreateFile( id );
        }
    }
    private boolean CreateFile( String ref ) {
        ReferenceFile file = new ReferenceFile( m_path.resolve(ref) );
        m_list.add( file );
        return true;
    }

    public void Refresh() {
        for( ReferenceFile file : m_list ) {
            file.Refresh();
        }
    }


    public String GetFirst() {
        return m_list.get(0).ReadValue();
    }

    private boolean IsSingle() {
        return m_single;
    }

    public boolean addRefs(List<String> newRefs){
        Iterator itr = newRefs.iterator();
        boolean ret = true;
        while (itr.hasNext()){
            String ref = (String) itr.next();
            ret = ret && CreateFile(ref);
        }
        return ret;
    }

    public boolean remRefs(List<String> remRefs){
        Iterator remItr = remRefs.iterator();
        while ( remItr.hasNext() ){
            String rem = (String) remItr.next();
            Iterator filItr = m_list.iterator();
            while ( filItr.hasNext() && !remRefs.isEmpty() ){
                ReferenceFile curr = (ReferenceFile) filItr.next();
                String ref = curr.m_fileLocation.getFileName().toString();
                if ( ref.equals(rem) ) {
                    m_list.remove(curr);
                    FileHelper.Delete( curr.m_fileLocation );
                }
            }
        }
        return remRefs.isEmpty();
    }

    public String toString(){
        String ret = "";
        Iterator<ReferenceFile> itrRef = m_list.iterator();
        while (itrRef.hasNext()) {
            ret += itrRef.next().toString() + ", ";
        }
        return ret;
    }

}
