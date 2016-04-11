package main;
import main.Entities.*;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Defines the calendar class for CoCal
 */
public class Calendar extends Collection {
    // a unique id for each calendar to have
    private static AtomicInteger __id = new AtomicInteger(0);
    private String getNextID() {
        Integer id = __id.getAndIncrement();
        String s = id.toString();
        m_id = id.intValue();
        return s;
    }

    public static void resetIDs(){
        __id = new AtomicInteger(0);
    }

    public Calendar( Path myPath, StringFile title, ReferenceList owner, ReferenceList events ) {
        m_filepath = myPath;
        m_attributes.put( "title",  title );
        m_references.put( "owner", owner );
        m_references.put( "events", events );
    }

    public Calendar( Path parentPath , List<String> data ) {
        Path path_events = parentPath.resolve( "calendars" );
        m_filepath = path_events.resolve( getNextID().toString() );
        FileHelper.CreateDirectory( m_filepath );

        m_attributes.put( "title",  new StringFile( data.get(0), m_filepath.resolve( "title" ) ) );
        m_references.put( "owner", new ReferenceList( data.get(1), m_filepath.resolve( "owner" ) ) );
        m_references.put( "events", new ReferenceList( m_filepath.resolve( "events" ) ) );
    }
}
