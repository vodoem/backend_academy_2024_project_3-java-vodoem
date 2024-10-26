package backend.academy.log_analyzer.file_writer;
import java.io.IOException;

public interface ReportFileWriter {
    void writeReportToFile(String directoryPath, String reportContent) throws IOException;
}