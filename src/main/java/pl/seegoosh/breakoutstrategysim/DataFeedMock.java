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

    private final EndDayEventPublisher eventPublisher;

    @Autowired
    public DataFeedMock(Environment environment, EndDayEventPublisher eventPublisher) {
        this.environment = environment;
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void init() {
        startFeeding();
    }

    public void startFeeding() {

        LOG.info("Simulation started");

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
                ClosingValueDTO closingValueDTO = new ClosingValueDTO(date, BigDecimal.valueOf(Long.parseLong(cells[4])));

                LOG.info("Received data from: {}", date);
                eventPublisher.publishEndDayEvent(closingValueDTO);
            }

            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        } catch (IOException e) {
            LOG.error("Invalid file location : {}. Execution stopping.", inputLocation);
        }
    }
}
