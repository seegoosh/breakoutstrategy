package pl.seegoosh.breakoutstrategysim.events;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.seegoosh.breakoutstrategysim.DataProcessor;

@Component
public class EndDayEventListener implements ApplicationListener<EndDayEvent> {

    private final DataProcessor dataProcessor;

    public EndDayEventListener(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    @Override
    public void onApplicationEvent(EndDayEvent endDayEvent) {
        dataProcessor.processClosingValue(endDayEvent.getValue());
    }
}
