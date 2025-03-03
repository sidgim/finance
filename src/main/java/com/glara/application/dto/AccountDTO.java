package com.glara.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO que representa una cuenta bancaria o tarjeta de crédito.")
public record AccountDTO(
        Long id,
        String name,
        BigDecimal currentBalance
) {}
