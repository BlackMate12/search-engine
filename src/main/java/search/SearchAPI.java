package search;

import db.DBHandler;

public class SearchAPI {
    private final DBHandler dbHandler;

    public SearchAPI(DBHandler dbHandler)
    {
        this.dbHandler = dbHandler;
    }

    public void search(String query)
    {
        dbHandler.searchFile(query);
    }

    //more later..........
}
