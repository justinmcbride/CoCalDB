package main.Entities;

import java.nio.file.Path;
import java.text.ParseException;

/**
 * Concrete class for File<Boolean>
 */
public class BooleanFile extends File<Boolean> {
    public BooleanFile(Boolean value, Path myPath ) {
        super( value, myPath );
    }
    public BooleanFile( String value, Path myPath ) {
        this(Boolean.parseBoolean(value), myPath );
    }

    public BooleanFile( Boolean value, Path myPath, boolean exists ) {
        super( value, myPath, exists );
    }

    public BooleanFile( String value, Path myPath, boolean exists ) {
        this( Boolean.parseBoolean(value), myPath, exists );
    }

    public Boolean ConvertValue(String s) throws ParseException {
        try {
            return Boolean.parseBoolean( s );
        }
        catch( NumberFormatException e ) {
            throw new ParseException( "couldn't convert " + s + " to boolean", 0 );
        }
    }


    //@Override
}
