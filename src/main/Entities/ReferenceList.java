package main.Entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by justinmcbride on 3/24/16.
 */
public class ReferenceList extends DirectoryMaker {
    private Boolean m_single;
    private ArrayList<ReferenceFile> m_list;
    private Path m_path;

    public ReferenceList( String id, Path myPath ) {
        m_single = true;
        m_path = myPath;
        m_list = new ArrayList<>();
        CreateDirectory( m_path );
        CreateFile( id );
    }

    private Boolean CreateFile( String ref ) {
        ReferenceFile file = new ReferenceFile( m_path.resolve(ref) );
        m_list.add( file );
        return true;
    }


    public ReferenceList( ArrayList<String> ids, Path myPath ) {
        m_single = false;
        m_path = myPath;
        m_list = new ArrayList<>();
        CreateDirectory( m_path );
        for( String id : ids ) {
            CreateFile( id );
        }
    }

    public String GetFirst() {
        return m_list.get(0).ReadValue();
    }

    private Boolean IsSingle() {
        return m_single;
    }

}
