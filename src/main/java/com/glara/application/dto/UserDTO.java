package com.glara.application.dto;

import com.glara.domain.entity.User;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "DTO que representa un usuario.")
public record UserDTO(Long id, String name, String email) {
    public UserDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}

