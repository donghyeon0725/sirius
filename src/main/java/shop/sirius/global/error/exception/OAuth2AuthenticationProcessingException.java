package shop.sirius.global.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import shop.sirius.global.error.ErrorCode;
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class OAuth2AuthenticationProcessingException extends RuntimeException {

    private ErrorCode errorCode;
    public OAuth2AuthenticationProcessingException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
