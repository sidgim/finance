package com.glara.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CategoryDTO(
        @Schema(hidden = true) Long id,

        @NotBlank(message = "Category name must not be empty")
        @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
        String name,

        @Size(max = 255, message = "Description must be at most 255 characters")
        String description) {
}

