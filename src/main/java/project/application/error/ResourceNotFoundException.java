package project.application.error;

import project.adapter.in.web.Utils.Code;

import java.util.Map;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(Code code, String message, Map<String, Object> context) {
        super(404, code, message, context);
    }
}
