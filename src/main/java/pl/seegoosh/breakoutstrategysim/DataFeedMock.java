package pl.seegoosh.breakoutstrategysim;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValueDTO;
import pl.seegoosh.breakoutstrategysim.events.EndDayEventPublisher;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class DataFeedMock {

    private static final Logger LOG = LoggerFactory.getLogger(DataFeedMock.class);
    private static final String FEED = "feed";
    public static final String CSV_HEADER = "dateTime,open,low,high,close";

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

        String inputLocation = environment.getProperty(FEED);

        if (StringUtils.isBlank(inputLocation)) {
            LOG.error("Feed file not provided. Execution stopping.");
            return;
        }
        LOG.info("Reading data from file: {}", inputLocation);

        try (FileInputStream fileInputStream = new FileInputStream(inputLocation);
             Scanner scanner = new Scanner(fileInputStream, StandardCharsets.UTF_8)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals(CSV_HEADER)){
                    continue;
                }
                String[] cells = line.split(",");
                LocalDate date = LocalDate.parse(cells[0], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                ClosingValueDTO closingValueDTO = new ClosingValueDTO(date, new BigDecimal(cells[4]));

                LOG.info("--------------");
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
