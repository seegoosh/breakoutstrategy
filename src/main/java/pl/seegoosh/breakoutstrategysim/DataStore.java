package pl.seegoosh.breakoutstrategysim;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
//this SHOULD be H2 - lack of time
public class DataStore {
    private Map<LocalDate, ClosingValue> storedClosingValues = new HashMap<>();
    private Map<LocalDate, BollingerBand> storedBands = new HashMap<>();

    public void addClosingValue(ClosingValue value){
        storedClosingValues.put(value.getDate(), value);
    }

    public List<ClosingValue> getClosingValuesFromLastPeriod(int numberOfDays){
        List<ClosingValue> result = new ArrayList<>();
        for (int i = numberOfDays; i >= 0 ; i--) {
            LocalDate day = LocalDate.now().minusDays(i);
            result.add(storedClosingValues.get(day));
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
