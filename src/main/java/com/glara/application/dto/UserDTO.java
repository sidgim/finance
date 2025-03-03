package com.glara.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "DTO que representa un usuario.")
public record UserDTO(
        Long id,
        String name,
        String email) {}
