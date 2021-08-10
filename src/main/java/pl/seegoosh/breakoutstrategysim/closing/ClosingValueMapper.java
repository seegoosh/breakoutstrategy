package pl.seegoosh.breakoutstrategysim.closing;

import org.springframework.stereotype.Component;
import pl.seegoosh.breakoutstrategysim.bands.BollingerBand;

@Component
public class ClosingValueMapper {

    public ClosingValue toClosingValue(ClosingValueDTO dto, BollingerBand band){
        ClosingValue closingValue = new ClosingValue();
        closingValue.setValue(dto.getValue());
        closingValue.setDate(dto.getDate());
        closingValue.setLowerBand(band.getLowerBand());
        closingValue.setUpperBand(band.getUpperBand());
        closingValue.setMovingAverage(band.getAverage());

        return closingValue;
    }
}
