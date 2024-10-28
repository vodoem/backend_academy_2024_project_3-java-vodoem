package backend.academy.log_analyzer.data;

import java.util.Map;

public class HttpStatus {
    private HttpStatus() {
    }

    private static final Integer CONTINUE = 100;
    private static final Integer SWITCHING_PROTOCOLS = 101;
    private static final Integer PROCESSING = 102;
    private static final Integer OK = 200;
    private static final Integer CREATED = 201;
    private static final Integer ACCEPTED = 202;
    private static final Integer NON_AUTHORITATIVE_INFORMATION = 203;
    private static final Integer NO_CONTENT = 204;
    private static final Integer RESET_CONTENT = 205;
    private static final Integer PARTIAL_CONTENT = 206;
    private static final Integer MULTI_STATUS = 207;
    private static final Integer ALREADY_REPORTED = 208;
    private static final Integer IM_USED = 226;
    private static final Integer MULTIPLE_CHOICES = 300;
    private static final Integer MOVED_PERMANENTLY = 301;
    private static final Integer FOUND = 302;
    private static final Integer SEE_OTHER = 303;
    private static final Integer NOT_MODIFIED = 304;
    private static final Integer USE_PROXY = 305;
    private static final Integer SWITCH_PROXY = 306;
    private static final Integer TEMPORARY_REDIRECT = 307;
    private static final Integer PERMANENT_REDIRECT = 308;
    private static final Integer BAD_REQUEST = 400;
    private static final Integer UNAUTHORIZED = 401;
    private static final Integer PAYMENT_REQUIRED = 402;
    private static final Integer FORBIDDEN = 403;
    private static final Integer NOT_FOUND = 404;
    private static final Integer METHOD_NOT_ALLOWED = 405;
    private static final Integer NOT_ACCEPTABLE = 406;
    private static final Integer PROXY_AUTHENTICATION_REQUIRED = 407;
    private static final Integer REQUEST_TIMEOUT = 408;
    private static final Integer CONFLICT = 409;
    private static final Integer GONE = 410;
    private static final Integer LENGTH_REQUIRED = 411;
    private static final Integer PRECONDITION_FAILED = 412;
    private static final Integer PAYLOAD_TOO_LARGE = 413;
    private static final Integer URI_TOO_LONG = 414;
    private static final Integer UNSUPPORTED_MEDIA_TYPE = 415;
    private static final Integer RANGE_NOT_SATISFIABLE = 416;
    private static final Integer EXPECTATION_FAILED = 417;
    private static final Integer IM_A_TEAPOT = 418;
    private static final Integer MISDIRECTED_REQUEST = 421;
    private static final Integer UNPROCESSABLE_ENTITY = 422;
    private static final Integer LOCKED = 423;
    private static final Integer FAILED_DEPENDENCY = 424;
    private static final Integer UPGRADE_REQUIRED = 426;
    private static final Integer PRECONDITION_REQUIRED = 428;
    private static final Integer TOO_MANY_REQUESTS = 429;
    private static final Integer REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
    private static final Integer UNAVAILABLE_FOR_LEGAL_REASONS = 451;
    private static final Integer INTERNAL_SERVER_ERROR = 500;
    private static final Integer NOT_IMPLEMENTED = 501;
    private static final Integer BAD_GATEWAY = 502;
    private static final Integer SERVICE_UNAVAILABLE = 503;
    private static final Integer GATEWAY_TIMEOUT = 504;
    private static final Integer HTTP_VERSION_NOT_SUPPORTED = 505;
    private static final Integer VARIANT_ALSO_NEGOTIATES = 506;
    private static final Integer INSUFFICIENT_STORAGE = 507;
    private static final Integer LOOP_DETECTED = 508;
    private static final Integer NOT_EXTENDED = 510;
    private static final Integer NETWORK_AUTHENTICATION_REQUIRED = 511;

    private static final Map<Integer, String> STATUS_NAMES = Map.<Integer, String>ofEntries(
        Map.entry(CONTINUE, "Continue"),
        Map.entry(SWITCHING_PROTOCOLS, "Switching Protocols"),
        Map.entry(PROCESSING, "Processing"),
        Map.entry(OK, "OK"),
        Map.entry(CREATED, "Created"),
        Map.entry(ACCEPTED, "Accepted"),
        Map.entry(NON_AUTHORITATIVE_INFORMATION, "Non-Authoritative Information"),
        Map.entry(NO_CONTENT, "No Content"),
        Map.entry(RESET_CONTENT, "Reset Content"),
        Map.entry(PARTIAL_CONTENT, "Partial Content"),
        Map.entry(MULTI_STATUS, "Multi-Status"),
        Map.entry(ALREADY_REPORTED, "Already Reported"),
        Map.entry(IM_USED, "IM Used"),
        Map.entry(MULTIPLE_CHOICES, "Multiple Choices"),
        Map.entry(MOVED_PERMANENTLY, "Moved Permanently"),
        Map.entry(FOUND, "Found"),
        Map.entry(SEE_OTHER, "See Other"),
        Map.entry(NOT_MODIFIED, "Not Modified"),
        Map.entry(USE_PROXY, "Use Proxy"),
        Map.entry(SWITCH_PROXY, "Switch Proxy"),
        Map.entry(TEMPORARY_REDIRECT, "Temporary Redirect"),
        Map.entry(PERMANENT_REDIRECT, "Permanent Redirect"),
        Map.entry(BAD_REQUEST, "Bad Request"),
        Map.entry(UNAUTHORIZED, "Unauthorized"),
        Map.entry(PAYMENT_REQUIRED, "Payment Required"),
        Map.entry(FORBIDDEN, "Forbidden"),
        Map.entry(NOT_FOUND, "Not Found"),
        Map.entry(METHOD_NOT_ALLOWED, "Method Not Allowed"),
        Map.entry(NOT_ACCEPTABLE, "Not Acceptable"),
        Map.entry(PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required"),
        Map.entry(REQUEST_TIMEOUT, "Request Timeout"),
        Map.entry(CONFLICT, "Conflict"),
        Map.entry(GONE, "Gone"),
        Map.entry(LENGTH_REQUIRED, "Length Required"),
        Map.entry(PRECONDITION_FAILED, "Precondition Failed"),
        Map.entry(PAYLOAD_TOO_LARGE, "Payload Too Large"),
        Map.entry(URI_TOO_LONG, "URI Too Long"),
        Map.entry(UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type"),
        Map.entry(RANGE_NOT_SATISFIABLE, "Range Not Satisfiable"),
        Map.entry(EXPECTATION_FAILED, "Expectation Failed"),
        Map.entry(IM_A_TEAPOT, "I'm a teapot"),
        Map.entry(MISDIRECTED_REQUEST, "Misdirected Request"),
        Map.entry(UNPROCESSABLE_ENTITY, "Unprocessable Entity"),
        Map.entry(LOCKED, "Locked"),
        Map.entry(FAILED_DEPENDENCY, "Failed Dependency"),
        Map.entry(UPGRADE_REQUIRED, "Upgrade Required"),
        Map.entry(PRECONDITION_REQUIRED, "Precondition Required"),
        Map.entry(TOO_MANY_REQUESTS, "Too Many Requests"),
        Map.entry(REQUEST_HEADER_FIELDS_TOO_LARGE, "Request Header Fields Too Large"),
        Map.entry(UNAVAILABLE_FOR_LEGAL_REASONS, "Unavailable For Legal Reasons"),
        Map.entry(INTERNAL_SERVER_ERROR, "Internal Server Error"),
        Map.entry(NOT_IMPLEMENTED, "Not Implemented"),
        Map.entry(BAD_GATEWAY, "Bad Gateway"),
        Map.entry(SERVICE_UNAVAILABLE, "Service Unavailable"),
        Map.entry(GATEWAY_TIMEOUT, "Gateway Timeout"),
        Map.entry(HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported"),
        Map.entry(VARIANT_ALSO_NEGOTIATES, "Variant Also Negotiates"),
        Map.entry(INSUFFICIENT_STORAGE, "Insufficient Storage"),
        Map.entry(LOOP_DETECTED, "Loop Detected"),
        Map.entry(NOT_EXTENDED, "Not Extended"),
        Map.entry(NETWORK_AUTHENTICATION_REQUIRED, "Network Authentication Required")
    );

    public static String getStatusName(int code) {
        return STATUS_NAMES.get(code);
    }
}
