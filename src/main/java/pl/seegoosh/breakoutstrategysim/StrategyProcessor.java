package pl.seegoosh.breakoutstrategysim;

import ch.algotrader.entity.Position;
import ch.algotrader.entity.trade.MarketOrder;
import ch.algotrader.enumeration.Direction;
import ch.algotrader.enumeration.Side;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StrategyProcessor {

    private final DataStore dataStore;

    @Value("${order-size}")
    private long orderSize;

    @Autowired
    public StrategyProcessor(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Optional<MarketOrder> makeOrder (ClosingValueDTO closingValueDTO, BollingerBand bollingerBand, Position currentPosition){
        Optional<MarketOrder> order = Optional.empty();

        if (closingValueDTO.getValue().compareTo(bollingerBand.getLowerBand()) < 0){
            order = Optional.of(new MarketOrder(Side.BUY, orderSize));
        }
        if (closingValueDTO.getValue().compareTo(bollingerBand.getUpperBand()) > 0){
            order = Optional.of(new MarketOrder(Side.BUY, -orderSize));
        }
        if (closingValueDTO.getValue().compareTo(bollingerBand.getAverage()) > 0
                && currentPosition.getDirection() == Direction.LONG){
            order = Optional.of(new MarketOrder(Side.SELL, orderSize));
        }
        if (closingValueDTO.getValue().compareTo(bollingerBand.getAverage()) < 0
                && currentPosition.getDirection() == Direction.SHORT){

        }
        return order;
    }
}
