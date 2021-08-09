package pl.seegoosh.breakoutstrategysim;

import org.springframework.context.ApplicationListener;

public class EndDayEventListener implements ApplicationListener<EndDayEvent> {
    @Override
    public void onApplicationEvent(EndDayEvent endDayEvent) {
        //process stuff here
    }
}
