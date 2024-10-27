package backend.academy.log_analyzer.file_writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AdocReportFileWriter implements ReportFileWriter{
    @Override
    public void writeReportToFile(String directoryPath, String reportContent) throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Создаем директорию, если ее нет
        }

        File file = new File(directory, "report.adoc");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(reportContent); // Запись в файл
        }
    }
}
