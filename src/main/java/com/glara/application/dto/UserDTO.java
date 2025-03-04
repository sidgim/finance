package com.glara.application.dto;

import com.glara.domain.model.Account;
import com.glara.domain.model.User;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO que representa un usuario.")
public record UserDTO(Long id, String name, String email) {
    public UserDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}

