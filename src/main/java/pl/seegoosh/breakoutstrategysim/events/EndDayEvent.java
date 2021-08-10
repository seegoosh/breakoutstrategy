package pl.seegoosh.breakoutstrategysim.events;

import org.springframework.context.ApplicationEvent;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValueDTO;

public class EndDayEvent extends ApplicationEvent {
    private ClosingValueDTO value;

    public EndDayEvent(Object source, ClosingValueDTO value) {
        super(source);
        this.value = value;
    }

    public ClosingValueDTO getValue() {
        return value;
    }
}
