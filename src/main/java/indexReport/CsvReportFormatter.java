package indexReport;

import java.io.*;

public class CsvReportFormatter implements IndexReportFormatter {
    private final PrintWriter writer;

    public CsvReportFormatter(String outputPath) {
        try {
            writer = new PrintWriter(new FileWriter(outputPath, false)); // overwrite
            writer.println("Name,Path,Score");
        } catch (IOException e) {
            throw new RuntimeException("Failed to open CSV report file", e);
        }
    }

    @Override
    public void report(File file, double score) {
        writer.printf("%s,%s,%.2f%n", file.getName(), file.getAbsolutePath(), score);
        writer.flush();
    }
}

