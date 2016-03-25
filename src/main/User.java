package main;
import main.Entities.*;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by Warren on 3/23/2016.
 * Defines the User class for CoCal
 */

public class User extends DirectoryMaker{
    static Integer __id = 0;
    static String getNextID() {
        String s = __id.toString();
        __id++;
        return s;
    }

    private Path m_filepath;
    StringFile m_name;
    StringFile m_email;
    StringFile m_password;
    BooleanFile m_isadmin;
    ReferenceList m_groups;
    ReferenceList m_calendar;

    public User(Path parentPath, String name) {
        m_filepath = parentPath.resolve( "users" );
        m_filepath = m_filepath.resolve( getNextID() );
        System.out.println( "Path: " + m_filepath );
        CreateDirectory( m_filepath );
        m_name = new StringFile( "", m_filepath.resolve( "name" ) );
        m_email = new StringFile( "", m_filepath.resolve( "email" ) );
        m_password = new StringFile( "", m_filepath.resolve( "password" ) );
        m_isadmin = new BooleanFile(false, m_filepath.resolve( "isadmin"));
        m_groups = new ReferenceList(m_filepath.resolve( "groups" ) );
        Calendar m_Cal = new Calendar(parentPath, name + "Calendar", name);
        m_calendar = new ReferenceList( name + "Calendar", m_filepath.resolve( "calendar" ) );
    }
}
