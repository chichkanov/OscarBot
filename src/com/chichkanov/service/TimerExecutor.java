package com.chichkanov.service;

import org.telegram.telegrambots.logging.BotLogger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by chichkanov on 20/10/2017.
 * telegram - @chichkanov777
 */
public class TimerExecutor {

    private static final String LOGTAG = "TIMEREXECUTOR";
    private static volatile TimerExecutor instance; ///< Instance
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1); ///< Thread to execute operations

    private TimerExecutor() {
    }

    public static TimerExecutor getInstance() {
        final TimerExecutor currentInstance;
        if (instance == null) {
            synchronized (TimerExecutor.class) {
                if (instance == null) {
                    instance = new TimerExecutor();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }

        return currentInstance;
    }

    public void startExecutionWithInterval(UpdateTask task, int interval) {
        BotLogger.warn(LOGTAG, "Posting new task");
        final Runnable taskWrapper = () -> {
            try {
                BotLogger.warn(LOGTAG, "Posted task with");
                task.execute();
                startExecutionWithInterval(task, interval);
            } catch (Exception e) {
                BotLogger.severe(LOGTAG, "Bot threw an unexpected exception at TimerExecutor", e);
            }
        };
        System.out.println("Rescheduling");
        executorService.schedule(taskWrapper, interval, TimeUnit.SECONDS);
    }
}
