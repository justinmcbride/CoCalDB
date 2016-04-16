package main;
import main.Entities.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static main.Entities.FileHelper.CreateDirectory;

/**
 * Defines the Group class for CoCal
 */
public class Group extends Collection {
    // a unique id for group event to have
    private static AtomicInteger __id  = new AtomicInteger(0);
    private String getNextID() {
        Integer id = __id.getAndIncrement();
        String s = id.toString();
        m_id = id.intValue();
        return s;
    }

    public static void resetIDs(){
        __id = new AtomicInteger(0);
    }

    public Group( Path parentPath, List<String> data ) {
        m_filepath = parentPath.resolve( "groups" );
        m_filepath = m_filepath.resolve( getNextID() );
        FileHelper.CreateDirectory( m_filepath );

        m_attributes.put( "name", new StringFile( "group" + __id, m_filepath.resolve( "name" ) ) );
        Calendar m_Cal = new Calendar(parentPath, Arrays.asList(data.get(0)+"Calendar",data.get(0)));
        m_references.put( "Calendar", new ReferenceList( data.get(0) + "Calendar", m_filepath.resolve( "calendar" ) ) );
    }

    public Group( Path myPath, StringFile name, ReferenceList members, ReferenceList calendar ) {
        m_filepath = myPath;
        m_attributes.put( "name", name );
        m_references.put( "members", members );
        m_references.put( "calendar", calendar );
    }

    public Group( Path parentPath, List<String> data, ArrayList<String> members_list ) {
        this(parentPath, data);
        m_references.put("members", new ReferenceList( members_list, m_filepath.resolve( "members" ) ) );
    }





}
