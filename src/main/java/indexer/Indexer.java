package indexer;

import db.DBConnection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
public class Indexer {
    private final DBConnection dbConnection;

    public Indexer(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void indexFile(File file) {
        if (file == null || !file.exists()) return;

        String fileName = file.getName();
        String filePath = file.getAbsolutePath();
        long fileSize = file.length();
        long lastModified = file.lastModified();
        String content = extractContent(file);

        dbConnection.insertOrUpdateFile(fileName, filePath, fileSize, lastModified, content);
        //System.out.println("Indexed: " + fileName);
    }

    private String extractContent(File file) {
        try {
            if (file.getName().endsWith(".txt"))
            {
                return new String(Files.readAllBytes(file.toPath()));
            }
        } catch (IOException e) {
            System.err.println("Failed to read content from: " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return null;
    }
}
