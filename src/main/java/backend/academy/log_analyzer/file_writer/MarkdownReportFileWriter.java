package backend.academy.log_analyzer.file_writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MarkdownReportFileWriter implements ReportFileWriter {
    private static final PrintStream PRINT_STREAM = System.out;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String OUTPUT_DIRECTORY = "reports";

    @Override
    public void writeReportToFile(String reportContent) throws IOException {
        File directory = new File(OUTPUT_DIRECTORY);

        if (!directory.exists()) {
            boolean dirsCreated = directory.mkdirs(); // Проверяем, была ли успешно создана директория
            if (!dirsCreated) {
                throw new IOException("Не удалось создать директорию: " + OUTPUT_DIRECTORY);
            }
        }

        File file = new File(directory, "report.md");
        try (FileWriter writer = new FileWriter(file, CHARSET)) {
            writer.write(reportContent); // Запись в файл
        }
        PRINT_STREAM.println("Результаты были занесены в файл " + OUTPUT_DIRECTORY + "/report.md");
    }
}
