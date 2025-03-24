package indexer;

import db.DBConnection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Indexer {
    private final DBConnection dbConnection;
    private static final Set<String> EXTRACTABLE_EXTENSIONS = new HashSet<>(Arrays.asList("txt"));

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
            String name = file.getName();
            int extensionIndex = name.lastIndexOf('.');
            if (extensionIndex != -1) {
                String extension = name.substring(extensionIndex + 1).toLowerCase();
                if(EXTRACTABLE_EXTENSIONS.contains(extension))
                    return new String(Files.readAllBytes(file.toPath()));
            }
            return null;
        } catch (IOException e) {
            System.err.println("Failed to read content from: " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return null;
    }
}
