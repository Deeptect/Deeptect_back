package com.deeptactback.deeptact_back.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final BaseResponseStatus errorCode;

    public BaseException(BaseResponseStatus errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static void sendErrorResponse(HttpServletResponse response, BaseResponseStatus status) throws IOException {
        response.setStatus(status.getHttpStatusCode().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        CMResponse<?> errorResponse = CMResponse.fail(status);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }
}
