package project.application.error;

import project.adapter.in.web.Utils.Code;

import java.util.Map;

public class ConflictException extends ApiException {
    public ConflictException(Code code, String message, Map<String, Object> context) {
        super(409, code, message, context);
    }
}