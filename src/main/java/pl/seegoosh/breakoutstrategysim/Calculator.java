package pl.seegoosh.breakoutstrategysim;

import org.springframework.stereotype.Component;
import pl.seegoosh.breakoutstrategysim.BollingerBand;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Calculator {

    private static final MathContext MC = new MathContext(10);

    public BollingerBand calculateBollingerBand(List<BigDecimal> values, LocalDate date){
        BigDecimal average = calculateAverage(values);

        BigDecimal standardDeviation = calculateAverage(values.stream()
                .map(value -> value.subtract(average)
                .pow(2, MC)).collect(Collectors.toList()))
                .sqrt(MC);

        BigDecimal doubleStandardDeviation = standardDeviation.multiply(BigDecimal.valueOf(2), MC);

        BigDecimal upper = average.add(doubleStandardDeviation);
        BigDecimal lower = average.subtract(doubleStandardDeviation);

        return new BollingerBand(date, upper, lower, average);
    }

    private BigDecimal calculateAverage(List<BigDecimal> values) {
        BigDecimal sum = values.stream().map(Objects::requireNonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(values.size()), RoundingMode.HALF_UP);
    }
}
