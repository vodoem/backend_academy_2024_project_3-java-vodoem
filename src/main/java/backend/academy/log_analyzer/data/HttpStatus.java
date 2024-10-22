package backend.academy.log_analyzer.data;

import java.util.HashMap;
import java.util.Map;

public class HttpStatus {
    private static final Map<Integer, String> statusNames = new HashMap<>();

    static {
        statusNames.put(100, "Continue");
        statusNames.put(101, "Switching Protocols");
        statusNames.put(102, "Processing");
        statusNames.put(200, "OK");
        statusNames.put(201, "Created");
        statusNames.put(202, "Accepted");
        statusNames.put(203, "Non-Authoritative Information");
        statusNames.put(204, "No Content");
        statusNames.put(205, "Reset Content");
        statusNames.put(206, "Partial Content");
        statusNames.put(207, "Multi-Status");
        statusNames.put(208, "Already Reported");
        statusNames.put(226, "IM Used");
        statusNames.put(300, "Multiple Choices");
        statusNames.put(301, "Moved Permanently");
        statusNames.put(302, "Found");
        statusNames.put(303, "See Other");
        statusNames.put(304, "Not Modified");
        statusNames.put(305, "Use Proxy");
        statusNames.put(306, "Switch Proxy");
        statusNames.put(307, "Temporary Redirect");
        statusNames.put(308, "Permanent Redirect");
        statusNames.put(400, "Bad Request");
        statusNames.put(401, "Unauthorized");
        statusNames.put(402, "Payment Required");
        statusNames.put(403, "Forbidden");
        statusNames.put(404, "Not Found");
        statusNames.put(405, "Method Not Allowed");
        statusNames.put(406, "Not Acceptable");
        statusNames.put(407, "Proxy Authentication Required");
        statusNames.put(408, "Request Timeout");
        statusNames.put(409, "Conflict");
        statusNames.put(410, "Gone");
        statusNames.put(411, "Length Required");
        statusNames.put(412, "Precondition Failed");
        statusNames.put(413, "Payload Too Large");
        statusNames.put(414, "URI Too Long");
        statusNames.put(415, "Unsupported Media Type");
        statusNames.put(416, "Range Not Satisfiable");
        statusNames.put(417, "Expectation Failed");
        statusNames.put(418, "I'm a teapot");
        statusNames.put(421, "Misdirected Request");
        statusNames.put(422, "Unprocessable Entity");
        statusNames.put(423, "Locked");
        statusNames.put(424, "Failed Dependency");
        statusNames.put(426, "Upgrade Required");
        statusNames.put(428, "Precondition Required");
        statusNames.put(429, "Too Many Requests");
        statusNames.put(431, "Request Header Fields Too Large");
        statusNames.put(451, "Unavailable For Legal Reasons");
        statusNames.put(500, "Internal Server Error");
        statusNames.put(501, "Not Implemented");
        statusNames.put(502, "Bad Gateway");
        statusNames.put(503, "Service Unavailable");
        statusNames.put(504, "Gateway Timeout");
        statusNames.put(505, "HTTP Version Not Supported");
        statusNames.put(506, "Variant Also Negotiates");
        statusNames.put(507, "Insufficient Storage");
        statusNames.put(508, "Loop Detected");
        statusNames.put(510, "Not Extended");
        statusNames.put(511, "Network Authentication Required");
    }

    public static String getStatusName(int statusCode) {
        return statusNames.getOrDefault(statusCode, "Unknown Status");
    }
}
