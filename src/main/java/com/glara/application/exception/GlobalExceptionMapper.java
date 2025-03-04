package com.glara.application.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.WebApplicationException;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String message = "An unexpected error occurred";

        if (exception instanceof WebApplicationException webEx) {
            status = webEx.getResponse().getStatus();
            message = webEx.getMessage();
        } else if (exception instanceof IllegalArgumentException) {
            status = Response.Status.BAD_REQUEST.getStatusCode();
            message = exception.getMessage();
        }

        return Response.status(status)
                .entity(new ErrorResponse(message, status))
                .build();
    }

    public static class ErrorResponse {
        public String message;
        public int status;

        public ErrorResponse(String message, int status) {
            this.message = message;
            this.status = status;
        }
    }
}


