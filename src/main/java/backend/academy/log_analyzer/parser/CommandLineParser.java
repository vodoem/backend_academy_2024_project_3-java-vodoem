package backend.academy.log_analyzer.parser;

import lombok.Getter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CommandLineParser {
    private final String[] args;
    @Getter private String path;
    @Getter private ZonedDateTime fromDate;
    @Getter private ZonedDateTime toDate;
    @Getter private String format;
    @Getter private String filterField;
    @Getter private String filterValue;

    public CommandLineParser(String[] args) {
        this.args = args;
        this.format = "markdown"; // Значение по умолчанию
    }
    public boolean parse() {
        if (args.length < 2 || !args[1].equals("--path")) {
            System.err.println("Использование: analyzer --path <путь> [--from <дата>] [--to <дата>] [--format <markdown|adoc>] [--filter-field <field>] [--filter-value <value>]");
            return false;
        }

        path = args[2];

        for (int i = 3; i < args.length; i++) {
            switch (args[i]) {
                case "--from":
                    if (i + 1 < args.length) {
                        fromDate = parseDate(args[++i]);
                    }
                    break;
                case "--to":
                    if (i + 1 < args.length) {
                        toDate = parseDate(args[++i]);
                    }
                    break;
                case "--format":
                    if (i + 1 < args.length) {
                        String formatArg = args[++i];
                        if (formatArg.equals("markdown") || formatArg.equals("adoc")) {
                            format = formatArg;
                        } else {
                            System.err.println("Допустимые форматы: markdown, adoc");
                            return false;
                        }
                    }
                    break;
                case "--filter-field":
                    if (i + 1 < args.length) {
                        filterField = args[++i];
                    }
                    break;
                case "--filter-value":
                    if (i + 1 < args.length) {
                        filterValue = removeQuotes(args[++i]);
                    }
                    break;
                default:
                    System.err.println("Неизвестный параметр: " + args[i]);
                    return false;
            }
        }
        return true;
    }

    // Метод для удаления кавычек в начале и конце строки, если они присутствуют
    private String removeQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static ZonedDateTime parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneId.systemDefault());
    }

}
