package indexReport;

import java.io.File;

public interface IndexReportFormatter {
    void report(File file, double score);
}

