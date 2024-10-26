package backend.academy.log_analyzer.file_writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MarkdownReportFileWriter implements ReportFileWriter{
    @Override
    public void writeReportToFile(String directoryPath, String reportContent) throws IOException {
        File directory = new File(directoryPath);
//        if (!directory.exists()) {
//            directory.mkdirs(); // Создаем директорию, если ее нет
//        }

        File file = new File(directory, "report.md");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(reportContent); // Запись в файл
        }
    }
}
