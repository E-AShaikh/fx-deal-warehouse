package com.bloomberg.fxdeals.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;


import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
public class FxDeal {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long Id;

    @NotNull(message = "From Currency ISO Code is missing.")
    @Pattern(regexp = "^[A-Z]{3}$", message = "From Currency ISO Code must be a 3-letter uppercase ISO code.")
    private String fromCurrencyISO;

    @NotNull(message = "To Currency ISO Code is missing.")
    @Pattern(regexp = "^[A-Z]{3}$", message = "To Currency ISO Code must be a 3-letter uppercase ISO code.")
    private String toCurrencyISO;

    @NotNull(message = "Deal timestamp is missing.")
    @PastOrPresent(message = "Deal timestamp must be in the past or present.")
    private LocalDateTime timestamp;

    @NotNull(message = "Deal Amount is missing.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Deal Amount must be greater than zero.")
    private BigDecimal amount;

}
