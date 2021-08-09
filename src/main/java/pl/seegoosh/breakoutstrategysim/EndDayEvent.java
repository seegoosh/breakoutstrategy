package pl.seegoosh.breakoutstrategysim;

import org.springframework.context.ApplicationEvent;

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
