package pl.seegoosh.breakoutstrategysim.bands;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValue;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValueDTO;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValueRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;

class CalculatorTest {

    private Calculator calculator;
    private ClosingValueRepository repository;
    private CalculatorProperties properties;

    @BeforeEach
    public void init(){
        repository = Mockito.mock(ClosingValueRepository.class);
        properties = Mockito.mock(CalculatorProperties.class);
    }

    @Test
    public void calculatorReturnsValue(){
        //given
        ClosingValue closingValue1 = new ClosingValue();
        closingValue1.setValue(new BigDecimal("2.5"));

        ClosingValue closingValue2 = new ClosingValue();
        closingValue2.setValue(new BigDecimal("3.5"));

        LocalDate today = LocalDate.of(2021, 8, 11);

        Mockito.when(properties.getCalculationPeriod()).thenReturn(2L);
        Mockito.when(repository.findClosingValuesSince(today.minusDays(properties.getCalculationPeriod())))
                .thenReturn(Arrays.asList(closingValue1, closingValue2));

        calculator = new Calculator(repository, properties);

        ClosingValueDTO closing = new ClosingValueDTO(today, new BigDecimal(2));

        //when
        BollingerBand bollingerBand = calculator.calculateBollingerBand(closing);

        //then
        assertEquals(BigDecimal.valueOf(1.451000).setScale(6, RoundingMode.DOWN), bollingerBand.getLowerBand());
    }

}