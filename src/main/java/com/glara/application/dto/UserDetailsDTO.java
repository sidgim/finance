package com.glara.application.dto;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDate;
import java.util.Date;

public record UserDetailsDTO(Long userId,
                             String userName,
                             String userEmail,
                             Long accountId,
                             String accountName,
                             BigDecimal currentBalance,
                             Long transactionId,
                             BigDecimal transactionAmount,
                             LocalDate transactionDate,
                             String transactionDescription,
                             String transactionType,
                             Long subscriptionId,
                             String subscriptionName,
                             BigDecimal subscriptionAmount,
                             Timestamp nextPayment) {
}
