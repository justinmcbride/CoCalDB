package main.Entities;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by justinmcbride on 3/24/16.
 */
public abstract class DirectoryMaker {
    protected Boolean CreateDirectory( Path path )
    {
        try {
            Files.createDirectories( path );
        }
        catch( FileAlreadyExistsException e ) {
            System.out.println( "\tdirectory already exists: " + e.getMessage() );
            return false;
        }
        catch( IOException e ) {
            System.out.println( "\tError creating directory: " + e.getMessage() );
            return false;
        }
        return true;
    }
}
