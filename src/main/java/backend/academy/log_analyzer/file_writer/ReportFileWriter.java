package backend.academy.log_analyzer.file_writer;

import java.io.IOException;

public interface ReportFileWriter {
    void writeReportToFile(String reportContent) throws IOException;
}
