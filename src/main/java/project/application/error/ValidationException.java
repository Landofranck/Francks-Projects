package project.application.error;

import project.adapter.in.web.Utils.Code;

import java.util.Map;

public class ValidationException extends ApiException {
    public ValidationException(Code code, String message, Map<String, Object> context) {
        super(400, code, message, context);
    }
}
