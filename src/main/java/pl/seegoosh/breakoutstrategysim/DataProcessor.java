package pl.seegoosh.breakoutstrategysim;

import ch.algotrader.entity.trade.MarketOrder;
import ch.algotrader.simulation.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.seegoosh.breakoutstrategysim.bands.BollingerBand;
import pl.seegoosh.breakoutstrategysim.bands.Calculator;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValue;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValueDTO;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValueMapper;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValueRepository;

import java.util.Optional;

@Service
public class DataProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(DataProcessor.class);

    private final Simulator simulator;
    private final Calculator calculator;
    private final StrategyProcessor strategyProcessor;
    private final ClosingValueRepository repository;
    private final ClosingValueMapper mapper;

    public DataProcessor(Simulator simulator, Calculator calculator, StrategyProcessor strategyProcessor, ClosingValueRepository repository, ClosingValueMapper mapper) {
        this.simulator = simulator;
        this.calculator = calculator;
        this.strategyProcessor = strategyProcessor;
        this.repository = repository;
        this.mapper = mapper;
    }

    public void processClosingValue(ClosingValueDTO closingValueDTO){
        LOG.info("Current market value is : {}", closingValueDTO.getValue());
        simulator.setCurrentPrice(closingValueDTO.getValue().doubleValue());

        BollingerBand band = calculator.calculateBollingerBand(closingValueDTO);

        ClosingValue closingValue = repository.save(mapper.toClosingValue(closingValueDTO, band));

        LOG.info("Storing calculated value : {}", closingValue.toString());
        Optional<MarketOrder> order = strategyProcessor.makeOrder(closingValue, simulator);

        order.ifPresent(simulator::sendOrder);
        LOG.info("Current balance is : {}", simulator.getCashBalance());

    }
}
