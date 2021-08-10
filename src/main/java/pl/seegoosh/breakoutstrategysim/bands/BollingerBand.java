package pl.seegoosh.breakoutstrategysim.bands;

import java.math.BigDecimal;

public class BollingerBand {
    private final BigDecimal upperBand;
    private final BigDecimal lowerBand;
    private final BigDecimal average;

    public BollingerBand(BigDecimal upperBand, BigDecimal lowerBand, BigDecimal average) {
        this.upperBand = upperBand;
        this.lowerBand = lowerBand;
        this.average = average;
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

}
