package pl.seegoosh.breakoutstrategysim;

import org.springframework.stereotype.Component;

@Component
public class FeedProcessor {

    private final FeedParser feedParser;

    public FeedProcessor(FeedParser feedParser) {
        this.feedParser = feedParser;
    }

    public void processDailyFeed(String dailyFeed) {

    }
}
