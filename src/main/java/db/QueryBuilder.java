package db;

public class QueryBuilder {

    public static String buildInsertQuery(String tableName, String[] columns) {
        String columnNames = String.join(", ", columns);
        String placeholders = String.join(", ", new String[columns.length]).replace("\0", "?");

        return "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";
    }

    public static String buildUpdateQuery(String tableName, String[] columns, String condition) {
        String setClause = String.join(" = ?, ", columns) + " = ?";
        return "UPDATE " + tableName + " SET " + setClause + " WHERE " + condition;
    }

    public static String buildSelectQuery(String tableName, String condition) {
        return "SELECT * FROM " + tableName + (condition != null ? " WHERE " + condition : "");
    }

    public static String buildDeleteQuery(String tableName, String condition) {
        return "DELETE FROM " + tableName + " WHERE " + condition;
    }
}

