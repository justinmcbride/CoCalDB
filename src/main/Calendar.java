package main;
import main.Entities.*;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Warren on 3/23/2016.
 * Defines the calendar class for CoCal
 */
public class Calendar extends DirectoryMaker {
    // a unique id for each calendar to have
    static AtomicInteger __id = new AtomicInteger();
    String getNextID() {
        Integer id = __id.getAndIncrement();
        String s = id.toString();
        m_ID = id.intValue();
        return s;
    }

    // the following are all attributes of the calendar

    public Calendar( Path myPath, StringFile title, ReferenceList owner, ReferenceList events ) {
        m_filepath = myPath;
        m_attributes.put( "title",  title );
        m_references.put( "owner", owner );
        m_references.put( "events", events );
    }


    public Calendar( Path parentPath , List<String> data ) {
        Path path_events = parentPath.resolve( "calendars" );
        m_filepath = path_events.resolve( getNextID().toString() );
        CreateDirectory( m_filepath );

        m_attributes.put( "title",  new StringFile( data.get(0), m_filepath.resolve( "title" ) ) );
        m_references.put( "owner", new ReferenceList( data.get(1), m_filepath.resolve( "owner" ) ) );
        m_references.put( "events", new ReferenceList( m_filepath.resolve( "events" ) ) );
    }
}
