package com.glara.infrastructure.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException exception) {
        Response originalResponse = exception.getResponse();

        // Si la excepción ya tiene un entity (JSON), la devolvemos como está
        if (originalResponse.hasEntity()) {
            return originalResponse;
        }

        // Si la excepción no tiene cuerpo, agregamos un JSON formateado
        return Response.status(originalResponse.getStatus())
                .entity(new GlobalExceptionMapper.ErrorResponse(exception.getMessage(), originalResponse.getStatus()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
