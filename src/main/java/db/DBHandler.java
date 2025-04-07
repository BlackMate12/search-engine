package db;

import java.sql.*;

public class DBHandler {
    private final Connection conn;

    public DBHandler()
    {
        try {
            this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to DB", e);
        }
    }

    //input from indexer
    public void insertOrUpdateFile(String name, String path, long size, long lastModified, String content)
    {
        String extension = extractExtension(name);
        String query = QueryBuilder.buildInsertOrUpdateFileQuery();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, path);
            stmt.setString(3, extension);
            stmt.setLong(4, size);
            stmt.setLong(5, lastModified);
            stmt.setString(6, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("DB insert/update failed for: " + path);
            e.printStackTrace();
        }
    }

    //input from searchapi
    public void searchFile(String userInput)
    {
        String query = QueryBuilder.buildSearchQuery();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userInput.replace(" ", " & "));
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                System.out.println("Found: " + rs.getString("name") + " at " + rs.getString("path"));
            }
        } catch (SQLException e) {
            System.err.println("Search query failed");
            e.printStackTrace();
        }
    }

    private String extractExtension(String name)
    {
        int dot = name.lastIndexOf('.');
        if (dot != -1 && dot < name.length() - 1)
        {
            return name.substring(dot + 1).toLowerCase();
        }
        return "";
    }
}
