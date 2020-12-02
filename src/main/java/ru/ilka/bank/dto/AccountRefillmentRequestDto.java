package ru.ilka.bank.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.ilka.bank.domain.CurrencyCodeEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRefillmentRequestDto {
    @Positive(message = "Refillment amount must be greater than 0.")
    BigDecimal amount;

    @NotNull(message = "Currency must be specified")
    CurrencyCodeEnum currencyCode;
}
