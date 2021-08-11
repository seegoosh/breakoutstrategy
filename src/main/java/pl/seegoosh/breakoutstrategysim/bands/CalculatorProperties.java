package pl.seegoosh.breakoutstrategysim.bands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties()
public class CalculatorProperties {

    private long calculationPeriod;

    public long getCalculationPeriod() {
        return calculationPeriod;
    }
}
