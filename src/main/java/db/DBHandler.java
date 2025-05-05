package db;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import observer.SearchSubject;

public class DBHandler {
    private final Connection conn;
    private final SearchSubject subject;

    public DBHandler(SearchSubject subject)
    {
        try {
            this.conn = DBConnection.getConnection();
            this.subject = subject;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to DB", e);
        }
    }

    //input from indexer
    public void insertOrUpdateFile(String name, String path, long size, long lastModified, String content, double score)
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
            stmt.setDouble(7, score);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("DB insert/update failed for: " + path);
            e.printStackTrace();
        }
    }

    //input from searchapi
    public void searchFile(String userInput) {
        subject.notifyObservers(userInput);
        Map<String, String> parsed = parseQuery(userInput);

        StringBuilder query = new StringBuilder("SELECT name, path, content FROM files WHERE ");
        boolean hasCondition = false;

        if (parsed.containsKey("path")) {
            query.append("path ILIKE ? ");
            hasCondition = true;
        }

        if (parsed.containsKey("content")) {
            if (hasCondition) query.append("AND ");
            query.append("(tsv_content @@ to_tsquery(?) OR name ILIKE ?) ");
            hasCondition = true;
        }

        if (parsed.containsKey("extension")) {
            if (hasCondition) query.append("AND ");
            query.append("extension = ? ");
            hasCondition = true;
        }

        if (!hasCondition) {
            query.append("(tsv_content @@ to_tsquery(?) OR name ILIKE ?) "); //default
        }

        query.append("ORDER BY score DESC");

        try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            int index = 1;
            if (parsed.containsKey("path")) {
                stmt.setString(index++, "%" + parsed.get("path") + "%");
            }
            if (parsed.containsKey("content")) {
                String contentInput = parsed.get("content").replace(" ", " & ");
                stmt.setString(index++, contentInput);
                stmt.setString(index++, "%" + parsed.get("content") + "%");
            }
            if (parsed.containsKey("extension")) {
                stmt.setString(index++, parsed.get("extension"));
            }
            if (!hasCondition) {
                String defaultInput = userInput.replace(" ", " & ");
                stmt.setString(index++, defaultInput);
                stmt.setString(index++, "%" + userInput + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String path = rs.getString("path");
                String content = rs.getString("content");

                System.out.println("Found: " + name + " at " + path);
                if (content != null && !content.trim().isEmpty()) {
                    String preview = content
                            .replaceAll("[\\r\\n]+", " ")
                            .trim();

                    if (preview.length() > 150) {
                        preview = preview.substring(0, 150) + "...";
                    }

                    System.out.println("Preview: " + preview);
                } else {
                    System.out.println("Preview: (No content available)");
                }
                System.out.println();
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

    private Map<String, String> parseQuery(String input) {
        Map<String, String> map = new HashMap<>();
        String[] tokens = input.split("\\s+");

        for (String token : tokens) {
            if (token.startsWith("path:")) {
                map.put("path", token.substring(5));
            } else if (token.startsWith("content:")) {
                map.put("content", token.substring(8));
            } else if (token.startsWith("extension:")) {
                map.put("extension", token.substring(10));
            }
        }
        return map;
    }
}
