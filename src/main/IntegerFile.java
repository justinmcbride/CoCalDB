package main;

import Entities.File;

import java.nio.file.Path;
import java.text.ParseException;

/**
 * Created by justinmcbride on 3/24/16.
 */
public class IntegerFile extends File<Integer> {
    @Override
    public Integer ConvertValue(String s) throws ParseException {
        try {
            return Integer.parseInt( s );
        }
        catch( NumberFormatException e ) {
            throw new ParseException( "couldn't convert " + s + " to int", 0 );
        }
    }

    public IntegerFile( Integer value, Path myPath ) {
        super( value, myPath );
    }
}
