package main;
import main.Entities.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Warren on 3/23/2016.
 * Defines the User class for CoCal
 */

public class User extends DirectoryMaker{
    static AtomicInteger __id  = new AtomicInteger();
    String getNextID() {
        Integer id = __id.getAndIncrement();
        String s = id.toString();
        m_ID = id.intValue();
        return s;
    }

    public User( Path myPath, StringFile name, StringFile email, StringFile password, BooleanFile isadmin, ReferenceList groups ) {
        m_filepath = myPath;

    }

    public User(Path parentPath, List<String> data) {
        m_filepath = parentPath.resolve( "users" );
        m_filepath = m_filepath.resolve( getNextID().toString() );
        System.out.println( "Path: " + m_filepath );
        CreateDirectory( m_filepath );

        m_attributes.put("name", new StringFile( "", m_filepath.resolve( "name" ) ) );
        m_attributes.put("email", new StringFile( "", m_filepath.resolve( "email" ) ) );
        m_attributes.put("password", new StringFile( "", m_filepath.resolve( "password" ) ) );
        m_attributes.put("isadmin", new BooleanFile(false, m_filepath.resolve( "isadmin") ) );
        m_references.put("groups", new ReferenceList(m_filepath.resolve( "groups" ) ) );

        Calendar m_Cal = new Calendar(parentPath, Arrays.asList(data.get(0)+"Calendar",data.get(1)));
        m_references.put("Calendar", new ReferenceList( data.get(1) + "Calendar", m_filepath.resolve( "calendar" ) ) );
    }


}

