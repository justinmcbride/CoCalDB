package main.Entities;

import jdk.nashorn.internal.parser.JSONParser;

/**
 * Parses JSON requests from an angularJS app and
 * spawns a Resolver thread to carry out the db operation
 */
class dbRequestThrd extends dbThrd {
    JSONParser parser;

    protected dbRequestThrd(){

    }
    public void run(){

    }
}
