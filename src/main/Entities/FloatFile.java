package main.Entities;

import main.Entities.File;

import java.nio.file.Path;
import java.text.ParseException;

/**
 * Created by justinmcbride on 3/24/16.
 * Concrete class for File<Float>
 */
public class FloatFile extends File<Float> {

    private FloatFile(Float value, Path myPath) {
        super( value, myPath );
    }

    public FloatFile( String value, Path myPath ) {
        this( Float.parseFloat(value), myPath );
    }

    private FloatFile(Float value, Path myPath, boolean exists) {
        super( value, myPath, exists );
    }

    public FloatFile( String value, Path myPath, boolean exists ) {
        this( Float.parseFloat(value), myPath, exists );
    }

    public Float ConvertValue(String s) throws ParseException {
        try {
            return Float.parseFloat( s );
        }
        catch( NumberFormatException e ) {
            throw new ParseException( "couldn't convert " + s + " to float", 0 );
        }
    }


    //@Override
}
