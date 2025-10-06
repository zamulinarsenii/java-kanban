package server;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public static HttpMethod fromString(String method) {
        if (method == null) {
            return null;
        }

        for (HttpMethod httpMethod : values()) {
            if (httpMethod.method.equalsIgnoreCase(method)) {
                return httpMethod;
            }
        }
        return null;
    }

    public static boolean isValid(String method) {
        return fromString(method) != null;
    }
}
