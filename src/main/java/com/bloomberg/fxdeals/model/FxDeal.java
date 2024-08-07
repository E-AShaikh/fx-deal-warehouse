package com.bloomberg.fxdeals.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "fx_deal")
public class FxDeal {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long Id;

    @NotNull(message = "From Currency ISO Code is missing.")
    @Pattern(regexp = "^[A-Z]{3}$", message = "From Currency ISO Code must be a 3-letter uppercase ISO code.")
    @Column(name = "from_currency_iso")
    private String fromCurrencyISO;

    @NotNull(message = "To Currency ISO Code is missing.")
    @Pattern(regexp = "^[A-Z]{3}$", message = "To Currency ISO Code must be a 3-letter uppercase ISO code.")
    @Column(name = "to_currency_iso")
    private String toCurrencyISO;

    @NotNull(message = "Deal timestamp is missing.")
    @PastOrPresent(message = "Deal timestamp must be in the past or present.")
    private LocalDateTime timestamp;

    @NotNull(message = "Deal Amount is missing.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Deal Amount must be greater than zero.")
    private BigDecimal amount;

    @NotNull(message = "Deal Type is missing.")
    private String dealType;

}
