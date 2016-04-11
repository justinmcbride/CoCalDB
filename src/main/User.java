package main;
import main.Entities.*;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Defines the User class for CoCal
 */

public class User extends Collection {
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

    public User( Path myPath, StringFile name, StringFile email, StringFile password, BooleanFile isadmin, ReferenceList groups ) {
        m_filepath = myPath;

    }

    public User(Path parentPath, List<String> data) {
        m_filepath = parentPath.resolve( "users" );
        m_filepath = m_filepath.resolve( getNextID().toString() );
        System.out.println( "Path: " + m_filepath );
        FileHelper.CreateDirectory( m_filepath );

        m_attributes.put("name", new StringFile( "", m_filepath.resolve( "name" ) ) );
        m_attributes.put("email", new StringFile( "", m_filepath.resolve( "email" ) ) );
        m_attributes.put("password", new StringFile( "", m_filepath.resolve( "password" ) ) );
        m_attributes.put("isadmin", new BooleanFile(false, m_filepath.resolve( "isadmin") ) );
        m_references.put("groups", new ReferenceList(m_filepath.resolve( "groups" ) ) );

        Calendar m_Cal = new Calendar(parentPath, Arrays.asList(data.get(0)+"Calendar",data.get(1)));
        m_references.put("Calendar", new ReferenceList( data.get(1) + "Calendar", m_filepath.resolve( "calendar" ) ) );
    }


}

