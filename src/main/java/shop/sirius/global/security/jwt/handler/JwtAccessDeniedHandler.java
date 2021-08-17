package shop.sirius.global.security.jwt.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import shop.sirius.global.error.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        throw new AccessDeniedException(ErrorCode.UNAUTHORIZED_VALUE.getMessage());
    }
}
