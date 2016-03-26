package main;
import main.Entities.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Warren on 3/23/2016.
 * Defines the Group class for CoCal
 */
public class Group extends DirectoryMaker {
    // a unique id for group event to have
    static Integer __id = 0;
    static String getNextID() {
        String s = __id.toString();
        __id++;
        return s;
    }

    private Path m_filepath;
    private StringFile m_name;
    private ReferenceList m_members;
    private ReferenceList m_calendar;

    public Group(Path parentPath, List<String> data ) {
        m_filepath = parentPath.resolve( "groups" );
        m_filepath = m_filepath.resolve( getNextID() );
        System.out.println( "Path: " + m_filepath );
        CreateDirectory( m_filepath );
        m_name = new StringFile( "group" + __id, m_filepath.resolve( "name" ) );
        Calendar m_Cal = new Calendar(parentPath, Arrays.asList(data.get(0)+"Calendar",data.get(0)));
        m_calendar = new ReferenceList( data.get(0) + "Calendar", m_filepath.resolve( "calendar" ) );
    }
    public Group(Path parentPath, List<String> data, ArrayList<String> members_list ) {
        this(parentPath, data);
        m_members = new ReferenceList( members_list, m_filepath.resolve( "members" ) );
    }


}
