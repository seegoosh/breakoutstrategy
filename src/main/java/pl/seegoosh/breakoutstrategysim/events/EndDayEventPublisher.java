package pl.seegoosh.breakoutstrategysim.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.seegoosh.breakoutstrategysim.closing.ClosingValueDTO;

@Component
public class EndDayEventPublisher {
    private ApplicationEventPublisher applicationEventPublisher;

    public EndDayEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishEndDayEvent(final ClosingValueDTO closingValueDTO){
        applicationEventPublisher.publishEvent(new EndDayEvent(this, closingValueDTO));
    }
}
