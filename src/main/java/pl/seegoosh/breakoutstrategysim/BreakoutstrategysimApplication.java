package pl.seegoosh.breakoutstrategysim;

import ch.algotrader.simulation.Simulator;
import ch.algotrader.simulation.SimulatorImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import pl.seegoosh.breakoutstrategysim.bands.CalculatorProperties;

@SpringBootApplication
@EnableConfigurationProperties(CalculatorProperties.class)
public class BreakoutstrategysimApplication {

    public static void main(String[] args) {
        SpringApplication.run(BreakoutstrategysimApplication.class, args);
    }

    @Bean
    public Simulator simulator(){
        Simulator simulator = new SimulatorImpl();
        simulator.setCashBalance(1000000.0);
        return simulator;
    }

}
