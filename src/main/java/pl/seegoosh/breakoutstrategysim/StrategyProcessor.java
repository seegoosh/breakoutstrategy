package pl.seegoosh.breakoutstrategysim;

import ch.algotrader.entity.trade.MarketOrder;
import ch.algotrader.enumeration.Direction;
import ch.algotrader.enumeration.Side;
import ch.algotrader.simulation.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Optional;

@Service
public class StrategyProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(StrategyProcessor.class);

    public Optional<MarketOrder> makeOrder(ClosingValue closingValue, Simulator simulator) {
        Optional<MarketOrder> order = Optional.empty();
        long allInOrder = BigDecimal.valueOf(simulator.getCashBalance()).divide(closingValue.getValue(), MathContext.DECIMAL32).longValue();

        boolean noCurrentPositionOpen = simulator.getPosition() == null || simulator.getPosition().getDirection() == Direction.FLAT;

        if (noCurrentPositionOpen && closingValue.getValue().compareTo(closingValue.getLowerBand()) < 0) {
            order = Optional.of(new MarketOrder(Side.BUY, allInOrder));
            LOG.info("Opening long position");
        }
        if (noCurrentPositionOpen && closingValue.getValue().compareTo(closingValue.getUpperBand()) > 0) {
            order = Optional.of(new MarketOrder(Side.SELL, allInOrder));
            LOG.info("Opening short position");
        }
        if (closingValue.getValue().compareTo(closingValue.getMovingAverage()) > 0
                && simulator.getPosition() != null && simulator.getPosition().getDirection() == Direction.LONG) {
            order = Optional.of(new MarketOrder(Side.SELL, simulator.getPosition().getQuantity()));
            LOG.info("Closing long position");
        }
        if (closingValue.getValue().compareTo(closingValue.getMovingAverage()) < 0
                && simulator.getPosition() != null && simulator.getPosition().getDirection() == Direction.SHORT) {
            order = Optional.of(new MarketOrder(Side.BUY, -simulator.getPosition().getQuantity()));
            LOG.info("Closing short position");
        }
        order.ifPresent(presentOrder -> LOG.info("Order created: {}", presentOrder));
        return order;
    }
}
