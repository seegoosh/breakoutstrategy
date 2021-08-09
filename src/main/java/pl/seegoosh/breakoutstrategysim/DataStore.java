package pl.seegoosh.breakoutstrategysim;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
//this SHOULD be H2 - lack of time
public class DataStore {
    private Map<LocalDate, ClosingValueDTO> storedClosingValues = new HashMap<>();
    private Map<LocalDate, BollingerBand> storedBands = new HashMap<>();

    public void addClosingValue(ClosingValueDTO value){
        storedClosingValues.put(value.getDate(), value);
    }

    public List<ClosingValueDTO> getClosingValuesFromLastPeriod(int numberOfDays){
        List<ClosingValueDTO> result = new ArrayList<>();
        for (int i = numberOfDays; i >= 0 ; i--) {
            LocalDate day = LocalDate.now().minusDays(i);
            ClosingValueDTO dayValue = storedClosingValues.get(day);
            if (null != dayValue){
                result.add(storedClosingValues.get(day));
            }
        }
        return result;
    }

    public int getClosingValuesSize(){
        return storedClosingValues.size();
    }

    public void addBand(BollingerBand band){
        storedBands.put(band.getDate(), band);
    }
}
