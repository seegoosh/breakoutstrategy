package pl.seegoosh.breakoutstrategysim;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClosingValue {
    private final LocalDate date;
    private final BigDecimal value;


    public ClosingValue(LocalDate date, BigDecimal value) {
        this.date = date;
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getValue() {
        return value;
    }
}
