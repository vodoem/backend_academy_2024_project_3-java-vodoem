package backend.academy.log_analyzer.data;

import java.util.Map;

public record LogReport(
    long totalRequests,
    Map<String, Long> mostRequestedResources,
    Map<Integer, Long> mostCommonResponseCodes,
    double averageResponseSize,
    long percentile95ResponseSize,
    long uniqueIpAddresses,
    long failedRequestsCount
) {
}
