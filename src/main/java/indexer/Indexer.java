package indexer;

import db.DBHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Indexer {
    private final DBHandler dbHandler;
    private static final Set<String> EXTRACTABLE_EXTENSIONS = new HashSet<>(Arrays.asList("txt"));

    public Indexer(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void indexFile(File file)
    {
        if (file == null || !file.exists()) return;

        String fileName = file.getName();
        String filePath = file.getAbsolutePath();
        long fileSize = file.length();
        long lastModified = file.lastModified(); //turn to readable time later
        String content = extractContent(file);
        double score = calculateScore(file);

        dbHandler.insertOrUpdateFile(fileName, filePath, fileSize, lastModified, content, score);
        //System.out.println("Indexed: " + fileName);
    }

    private String extractContent(File file)
    {
        try {
            String name = file.getName();
            int extensionIndex = name.lastIndexOf('.');
            if (extensionIndex != -1)
            {
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

    private double calculateScore(File file) {
        String path = file.getAbsolutePath();
        int pathDepth = path.split(File.separator.equals("\\") ? "\\\\" : File.separator).length;

        double baseScore = file.lastModified()/1000.0/60.0/60.0/24.0;
        double aux = baseScore/2;

        baseScore += aux / pathDepth;  // shorter path = higher score

        return baseScore;
    }
}
