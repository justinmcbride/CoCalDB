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
    StringFile name;
    StringFile email;
    StringFile password;
    BooleanFile isadmin;
    ReferenceList groups;
    ReferenceList calendar;

    public User(Path parentPath) {
        m_filepath = parentPath.resolve( "users" );
        m_filepath = m_filepath.resolve( getNextID() );
        System.out.println( "Path: " + m_filepath );
        CreateDirectory( m_filepath );
        name = new StringFile( "", m_filepath.resolve( "name" ) );
        email = new StringFile( "", m_filepath.resolve( "email" ) );
        password = new StringFile( "", m_filepath.resolve( "password" ) );
        isadmin = new BooleanFile(false, m_filepath.resolve( "isadmin"));
        groups = new ReferenceList(m_filepath.resolve( "groups" ) );
        Calendar m_Cal = new Calendar(parentPath);
        calendar = new ReferenceList( name + "Calendar", m_filepath.resolve( "calendar" ) );
    }
}
