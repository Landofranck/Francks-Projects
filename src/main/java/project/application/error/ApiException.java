package project.application.error;

import project.adapter.in.web.Utils.Code;

import java.util.Collections;
import java.util.Map;

public abstract class ApiException extends RuntimeException {
    private final int status;
    private final Code code;
    private final Map<String, Object> context;

    protected ApiException(int status, Code code, String message, Map<String, Object> context) {
        super(message);
        this.status = status;
        this.code = code;
        this.context = context == null ? Collections.emptyMap() : Collections.unmodifiableMap(context);
    }

    public int getStatus() { return status; }
    public Code getCode() { return code; }
    public Map<String, Object> getContext() { return context; }
}
