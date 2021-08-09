package pl.seegoosh.breakoutstrategysim;

import ch.algotrader.entity.trade.MarketOrder;
import ch.algotrader.simulation.Simulator;
import ch.algotrader.simulation.SimulatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class DataFeedMock {

    private static final Logger LOG = LoggerFactory.getLogger(DataFeedMock.class);

    private final Environment environment;
    private final DataStore dataStore;
    private final Calculator calculator;
    private final StrategyProcessor strategyProcessor;

    //should use properties class - no time;
    @Value("${calculation-period}")
    private int calculationPeriod;

    @Autowired
    public DataFeedMock(Environment environment, DataStore dataStore, Calculator calculator, StrategyProcessor strategyProcessor) {
        this.environment = environment;
        this.dataStore = dataStore;
        this.calculator = calculator;
        this.strategyProcessor = strategyProcessor;
    }

    @PostConstruct
    public void init() {
        startFeeding();
    }

    //should not do logic within a stream from file, but pass to another stream processed outside - not enough time
    public void startFeeding() {

        Simulator simulator = new SimulatorImpl();
        simulator.setCashBalance(1000000.0);

        LOG.info("Processing started");

        String inputLocation = environment.getProperty("feed");

        if (StringUtils.isBlank(inputLocation)) {
            LOG.error("Feed file not provided. Execution stopping.");
            return;
        }
        LOG.info("Reading data from file: {}", inputLocation);

        try (FileInputStream fileInputStream = new FileInputStream(inputLocation);
             Scanner scanner = new Scanner(fileInputStream, StandardCharsets.UTF_8)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] cells = line.split(",");
                LocalDate date = LocalDate.parse(cells[0], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                ClosingValue closingValue = new ClosingValue(date, BigDecimal.valueOf(Long.parseLong(cells[4])));
                dataStore.addClosingValue(closingValue);

                simulator.setCurrentPrice(closingValue.getValue().doubleValue());

                if (dataStore.getClosingValuesSize() < calculationPeriod) {
                    LOG.debug("Not enough data to calculate Bollinger Band with given configuration.");
                    continue;
                }

                BollingerBand band = calculator.calculateBollingerBand(dataStore.getClosingValuesFromLastPeriod(calculationPeriod)
                        .stream().map(ClosingValue::getValue).collect(Collectors.toList()), date);
                LOG.info("Storing calculated value : {}", band.toString());
                dataStore.addBand(band);
                Optional<MarketOrder> order = strategyProcessor.makeOrder(closingValue, band, simulator.getPosition());

                order.ifPresent(simulator::sendOrder);
            }

            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        } catch (IOException e) {
            LOG.error("Invalid file location : {}. Execution stopping.", inputLocation);
        }
    }
}
