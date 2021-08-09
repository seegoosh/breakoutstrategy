package pl.seegoosh.breakoutstrategysim;

import ch.algotrader.entity.trade.MarketOrder;
import ch.algotrader.simulation.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DataProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(DataProcessor.class);

    private final Simulator simulator;
    private final Calculator calculator;
    private final StrategyProcessor strategyProcessor;
    private final DataStore dataStore;

    //should use properties class - no time;
    @Value("${calculation-period}")
    private int calculationPeriod;

    public DataProcessor(Simulator simulator, Calculator calculator, StrategyProcessor strategyProcessor, DataStore dataStore) {
        this.simulator = simulator;
        this.calculator = calculator;
        this.strategyProcessor = strategyProcessor;
        this.dataStore = dataStore;
    }

    public void processClosingValue(ClosingValueDTO closingValueDTO){
        simulator.setCurrentPrice(closingValueDTO.getValue().doubleValue());

        BollingerBand band = calculator.calculateBollingerBand(dataStore.getClosingValuesFromLastPeriod(calculationPeriod)
                .stream().map(ClosingValueDTO::getValue).collect(Collectors.toList()), closingValueDTO.getDate());
        LOG.info("Storing calculated value : {}", band.toString());
        dataStore.addBand(band);
        Optional<MarketOrder> order = strategyProcessor.makeOrder(closingValueDTO, band, simulator.getPosition());

        order.ifPresent(simulator::sendOrder);
    }
}
