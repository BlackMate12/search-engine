package indexReport;

import java.io.File;

public class ConsoleReportFormatter implements IndexReportFormatter {
    @Override
    public void report(File file, double score) {
        System.out.println("[Indexed] " + file.getAbsolutePath() + " | Score: " + score);
    }
}

