package com.botscrew.botframework.sender;

import com.botscrew.botframework.domain.user.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public abstract class AbstractSender<B extends Bot, M extends Message> implements Sender<B, M> {
    @Autowired
    @Qualifier("senderTaskScheduler")
    private ThreadPoolTaskScheduler scheduler;

    @Override
    public ScheduledFuture send(B bot, M message, int delay) {
        return scheduler.schedule(() -> send(bot, message), currentDatePlusMillis(delay));
    }

    private Date currentDatePlusMillis(int millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MILLISECOND, millis);
        return calendar.getTime();
    }
}
