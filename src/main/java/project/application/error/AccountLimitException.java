package project.application.error;

import project.adapter.in.web.Utils.Code;

import java.util.Map;

public class AccountLimitException extends ApiException{
    public AccountLimitException(Code code,String message, Map<String, Object> context) {
        super(422, code, message, context);
    }
}
