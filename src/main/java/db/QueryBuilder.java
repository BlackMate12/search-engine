package db;

public class QueryBuilder {

    public static String buildInsertOrUpdateFileQuery()
    {
        return "INSERT INTO files (name, path, extension, size, last_modified, content) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (path) DO UPDATE SET " +
                "name = EXCLUDED.name, extension = EXCLUDED.extension, size = EXCLUDED.size, " +
                "last_modified = EXCLUDED.last_modified, content = EXCLUDED.content";
    }

    public static String buildSearchQuery()
    {
        return "SELECT name, path FROM files WHERE tsv_content @@ to_tsquery(?)";
    }

    public static String buildDeleteQuery()
    {
        return "DELETE FROM files"; //deletes everything, add back conditions and stuff later if needed lol
    }
}

