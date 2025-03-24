package crawler;

import indexer.Indexer;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Crawler {
    private final Indexer indexer;
    private static final Set<String> IGNORED_EXTENSIONS = new HashSet<>(Arrays.asList("exe", "tmp"));

    public Crawler(Indexer indexer)
    {
        this.indexer = indexer;
    }
    public void crawlDirectory(File directory)
    {
        if (directory == null || !directory.exists()) {
            System.out.println("Invalid directory: " + directory);
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                crawlDirectory(file);
            } else {
                if (!ignoreFile(file)) {
                    this.indexer.indexFile(file);
                    //extractMetadata(file);
                }
            }
        }
    }

    /*
    private static void extractMetadata(File file)
    {
        System.out.println("File: " + file.getName());
        System.out.println("  Path: " + file.getAbsolutePath());
        System.out.println("  Size: " + file.length() + " bytes");
        System.out.println("  Last Modified: " + file.lastModified());
        System.out.println("---------------------------------");
    }
    */

    private static boolean ignoreFile(File file)
    {
        String name = file.getName();
        int extensionIndex = name.lastIndexOf('.');
        if (extensionIndex != -1) {
            String extension = name.substring(extensionIndex + 1).toLowerCase();
            return IGNORED_EXTENSIONS.contains(extension);
        }
        return false;
    }

    /* test
    public static void main(String[] args)
    {
        // Example usage - Set root directory
        File rootDir = new File("D:\\test-folder"); // Change this path accordingly
        crawlDirectory(rootDir);
    }
    */
}
