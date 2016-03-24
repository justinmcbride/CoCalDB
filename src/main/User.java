package main;
import Entities.*;
import java.util.ArrayList;

/**
 * Created by Warren on 3/23/2016.
 * Defines the User class for cocal
 */

public class User {
    StringFile name;
    StringFile email;
    StringFile password;
    File<Boolean> isadmin;
    ArrayList<ID> groups;
    ID calendar;

}
