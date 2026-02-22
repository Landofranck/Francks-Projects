package project.application.error;

import project.adapter.in.web.Utils.Code;

import java.util.Map;

public class InsufficientFundsException extends ApiException {
    public InsufficientFundsException(String message, Map<String, Object> context) {
        super(422, Code.INSUFFICIENT_FUNDS, message, context);
    }
}