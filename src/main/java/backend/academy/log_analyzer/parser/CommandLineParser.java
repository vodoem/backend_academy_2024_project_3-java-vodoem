package backend.academy.log_analyzer.parser;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import lombok.Getter;

public class CommandLineParser {

    private static final String MARKDOWN = "markdown";
    private static final String FORMAT_ADOC = "adoc";
    private static final String FROM_ARG = "--from";
    private static final String TO_ARG = "--to";
    private static final String FORMAT_ARG = "--format";
    private static final String FILTER_FIELD_ARG = "--filter-field";
    private static final String FILTER_VALUE_ARG = "--filter-value";
    private static final String PATH_ARG = "--path";

    private static final int ADDITIONAL_ARGS_START_INDEX = 3;
    private static final int MIN_ARG_COUNT = 2;
    private static final int PATH_INDEX = 2;

    private static final Set<String> ARGUMENTS_REQUIRING_NEXT_ARGUMENT = new HashSet<>();

    static {
        ARGUMENTS_REQUIRING_NEXT_ARGUMENT.add(FROM_ARG);
        ARGUMENTS_REQUIRING_NEXT_ARGUMENT.add(TO_ARG);
        ARGUMENTS_REQUIRING_NEXT_ARGUMENT.add(FORMAT_ARG);
        ARGUMENTS_REQUIRING_NEXT_ARGUMENT.add(FILTER_FIELD_ARG);
        ARGUMENTS_REQUIRING_NEXT_ARGUMENT.add(FILTER_VALUE_ARG);
    }

    private final List<String> args;
    @Getter private String path;
    @Getter private ZonedDateTime fromDate;
    @Getter private ZonedDateTime toDate;
    @Getter private String format;
    @Getter private String filterField;
    @Getter private String filterValue;

    public CommandLineParser(String[] args) {
        this.args = List.of(args);
        this.format = MARKDOWN; // Значение по умолчанию
    }

    public boolean parse() {
        if (!isValidInitialArguments()) {
            return false;
        }

        path = args.get(PATH_INDEX);
        return processArguments();
    }

    private boolean isValidInitialArguments() {
        if (args.size() < MIN_ARG_COUNT || !PATH_ARG.equalsIgnoreCase(args.get(1))) {
            System.err.println(
                "Использование: analyzer --path <путь> [--from <дата>] [--to <дата>] "
                    + "[--format <markdown|adoc>] [--filter-field <field>] [--filter-value <value>]");
            return false;
        }
        return true;
    }

    private boolean processArguments() {
        ListIterator<String> iterator = args.listIterator(ADDITIONAL_ARGS_START_INDEX);
        boolean valid = true;

        while (iterator.hasNext()) {
            String arg = iterator.next();
            String nextArg = iterator.hasNext() ? iterator.next() : null;

            if (!processSingleArgument(arg, nextArg)) {
                valid = false;
            }

            if (requiresNextArgument(arg) && nextArg != null) {
                // Пропускаем следующий аргумент (он уже обработан)
                continue;
            }

            if (nextArg != null) {
                iterator.previous(); // Возвращаемся на шаг назад, так как nextArg был пропущен
            }
        }

        return valid;
    }

    private boolean processSingleArgument(String arg, String nextArg) {
        boolean isProcess;
        switch (arg) {
            case FROM_ARG -> isProcess = processFromDate(nextArg);
            case TO_ARG -> isProcess = processToDate(nextArg);
            case FORMAT_ARG -> isProcess = processFormat(nextArg);
            case FILTER_FIELD_ARG -> isProcess = processFilterField(nextArg);
            case FILTER_VALUE_ARG -> isProcess = processFilterValue(nextArg);
            default -> {
                System.err.println("Неизвестный параметр: " + arg);
                return false;
            }
        }
        return isProcess;
    }

    private boolean requiresNextArgument(String arg) {
        return ARGUMENTS_REQUIRING_NEXT_ARGUMENT.contains(arg);
    }

    private boolean processFromDate(String nextArg) {
        if (nextArg != null) {
            fromDate = parseDate(nextArg);
        }
        return true;
    }

    private boolean processToDate(String nextArg) {
        if (nextArg != null) {
            toDate = parseDate(nextArg);
        }
        return true;
    }

    private boolean processFormat(String nextArg) {
        if (nextArg != null) {
            if (MARKDOWN.equalsIgnoreCase(nextArg) || FORMAT_ADOC.equalsIgnoreCase(nextArg)) {
                format = nextArg;
            } else {
                System.err.println("Допустимые форматы: markdown, adoc");
                return false;
            }
        }
        return true;
    }

    private boolean processFilterField(String nextArg) {
        if (nextArg != null) {
            filterField = nextArg;
        }
        return true;
    }

    private boolean processFilterValue(String nextArg) {
        if (nextArg != null) {
            filterValue = removeQuotes(nextArg);
        }
        return true;
    }

    private static ZonedDateTime parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneId.systemDefault());
    }

    private String removeQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
