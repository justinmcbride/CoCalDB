package main;
import main.Entities.*;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by Warren on 3/23/2016.
 * Defines the calendar class for CoCal
 */
public class Calendar extends DirectoryMaker{
    // a unique id for each calendar to have
    static Integer __id = 0;
    static String getNextID() {
        String s = __id.toString();
        __id++;
        return s;
    }
    private Path m_filepath; // the path to this specific calendar's directory

    // the following are all attributes of the calendar
    private StringFile m_name;
    private ReferenceList m_owner;
    private ReferenceList m_events;


    public Calendar(Path parentPath , List<String> data) {
        Path path_events = parentPath.resolve( "calendars" );
        //System.out.println( "Path in: " + path_events.toString() );
        m_filepath = path_events.resolve( getNextID().toString() );
        CreateDirectory( m_filepath );
        //new File(m_filepath.toString()).mkdirs();                // placed here to not repeat with each attribute
        //System.out.println( "Path: " + m_filepath );

        m_name = new StringFile( data.get(0), m_filepath.resolve( "title" ) );
        m_owner = new ReferenceList( data.get(1), m_filepath.resolve( "owners" ) );
        m_events = new ReferenceList( m_filepath.resolve( "events" ) );
    }
}
