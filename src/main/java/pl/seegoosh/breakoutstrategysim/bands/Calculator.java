package pl.seegoosh.breakoutstrategysim.bands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValue;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValueDTO;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValueRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Calculator {
    private final ClosingValueRepository repository;

    @Value("${calculation-period}")
    private int calculationPeriod;

    public Calculator(ClosingValueRepository repository) {
        this.repository = repository;
    }

    public BollingerBand calculateBollingerBand(ClosingValueDTO currentClosing){
        List<BigDecimal> values = getValuesForCalculatedPeriod(currentClosing);

        BigDecimal average = calculateAverage(values);

        BigDecimal standardDeviation = calculateAverage(values.stream()
                .map(value -> value.subtract(average)
                .pow(2, MathContext.DECIMAL32)).collect(Collectors.toList()))
                .sqrt(MathContext.DECIMAL32);

        BigDecimal doubleStandardDeviation = standardDeviation.multiply(BigDecimal.valueOf(2), MathContext.DECIMAL32);

        BigDecimal upper = average.add(doubleStandardDeviation);
        BigDecimal lower = average.subtract(doubleStandardDeviation);

        return new BollingerBand(upper, lower, average);
    }

    private List<BigDecimal> getValuesForCalculatedPeriod(ClosingValueDTO currentClosing) {
        List<BigDecimal> values = repository.findClosingValuesSince(currentClosing.getDate().minusDays(calculationPeriod))
                .stream().map(ClosingValue::getValue).collect(Collectors.toList());

        values.add(currentClosing.getValue());
        return values;
    }

    private BigDecimal calculateAverage(List<BigDecimal> values) {
        BigDecimal sum = values.stream().map(Objects::requireNonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(values.size()), RoundingMode.HALF_UP);
    }
}
