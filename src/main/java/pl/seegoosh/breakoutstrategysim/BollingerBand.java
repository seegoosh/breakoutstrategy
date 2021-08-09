package pl.seegoosh.breakoutstrategysim;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BollingerBand {
    private final LocalDate date;
    private final BigDecimal upperBand;
    private final BigDecimal lowerBand;
    private final BigDecimal average;

    public BollingerBand(LocalDate date, BigDecimal upperBand, BigDecimal lowerBand, BigDecimal average) {
        this.date = date;
        this.upperBand = upperBand;
        this.lowerBand = lowerBand;
        this.average = average;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getUpperBand() {
        return upperBand;
    }

    public BigDecimal getLowerBand() {
        return lowerBand;
    }

    public BigDecimal getAverage() {
        return average;
    }

    @Override
    public String toString() {
        return new StringBuilder("Bollinger band for day : ")
                .append(date)
                .append(System.lineSeparator())
                .append("upper band : ")
                .append(upperBand.toString())
                .append(System.lineSeparator())
                .append("lower band : ")
                .append(lowerBand.toString())
                .append(System.lineSeparator())
                .append("average : ")
                .append(average).toString();
    }
}
