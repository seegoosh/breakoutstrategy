package pl.seegoosh.breakoutstrategysim.closing;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class ClosingValue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate date;
    private BigDecimal value;
    private BigDecimal lowerBand;
    private BigDecimal upperBand;
    private BigDecimal movingAverage;

    public BigDecimal getLowerBand() {
        return lowerBand;
    }

    public void setLowerBand(BigDecimal lowerBand) {
        this.lowerBand = lowerBand;
    }

    public BigDecimal getUpperBand() {
        return upperBand;
    }

    public void setUpperBand(BigDecimal upperBand) {
        this.upperBand = upperBand;
    }

    public BigDecimal getMovingAverage() {
        return movingAverage;
    }

    public void setMovingAverage(BigDecimal movingAverage) {
        this.movingAverage = movingAverage;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {

        return "Closing value on " + this.date +
                " is: " + this.value +
                ". Calculated Bollinger Band values are : " +
                "upper band - " + this.upperBand +
                ", lower band - " + this.lowerBand +
                ", moving average - " + this.movingAverage;
    }
}
