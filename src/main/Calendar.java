package main;
import main.Entities.*;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Warren on 3/23/2016.
 * Defines the calendar class for CoCal
 */
public class Calendar {
    // a unique id for each calendar to have
    static Integer __id = 0;
    static String getNextID() {
        String s = __id.toString();
        __id++;
        return s;
    }
    private Path m_filepath; // the path to this specific event's directory

    // the following are all attributes of the calendar
    private StringFile name;
    private idFile owner;
    private idFile events; //ArrayList<id> events;

    public Calendar( Path parentPath ) {
        Path path_events = parentPath.resolve( "calendars" );
        System.out.println( "Path in: " + path_events.toString() );
        m_filepath = path_events.resolve( getNextID().toString() );
        new File(m_filepath.toString()).mkdirs();                // placed here to not repeat with each attribute
        System.out.println( "Path: " + m_filepath );

        name = new StringFile( "name", m_filepath.resolve( "title" ) );
        //owner = new idFile( 0, m_filepath.resolve( "cost" ) );
        //events = new idFile(1, m_filepath.resolve( "location" ) );
    }
}
