package main.Entities;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by Justin on 3/30/2016.
 */
public class FileHelper {
  public static Boolean CreateDirectory( Path path ) {
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


  public static boolean Delete( Path path_to_delete ) {
    try {
      Files.walkFileTree( path_to_delete, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
        {
          // System.out.println( "Deleting file: " + file );
          Files.delete( file );
          return FileVisitResult.CONTINUE;
        }
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException
        {
          if ( e == null ) {
            // System.out.println( "Deleting directory: " + dir );
            Files.delete( dir );
            return FileVisitResult.CONTINUE;
          }
          else {
            // directory iteration failed
            throw e;
          }
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
