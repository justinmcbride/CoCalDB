package main.Entities;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Object to hold list of references to other db collection documents
 */
public class ReferenceList extends DirectoryMaker {
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
        CreateDirectory( m_path );
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
    private Boolean CreateFile( String ref ) {
        ReferenceFile file = new ReferenceFile( m_path.resolve(ref) );
        m_list.add( file );
        return true;
    }

    public String GetFirst() {
        return m_list.get(0).ReadValue();
    }

    private Boolean IsSingle() {
        return m_single;
    }

}
