package com.chichkanov;

import com.chichkanov.service.OscarService;
import com.chichkanov.service.TimerExecutor;
import com.chichkanov.service.UpdateTask;
import com.chichkanov.util.BotParams;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class OscarBot extends TelegramLongPollingBot {

    private static final String LOGTAG = "OSCARBOT";

    OscarBot() throws TelegramApiException {
        startUpdateTimer();
    }

    private void startUpdateTimer() throws TelegramApiException {
        TimerExecutor.getInstance()
                .startExecutionWithInterval(new UpdateTask() {
                    @Override
                    public void execute() {
                        try {
                            sendMessages();
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }, BotParams.UPDATE_INTERVAL_SECONDS);
    }

    @Override
    public void onUpdateReceived(Update update) {
        BotParams.userToChat.add(update.getMessage().getChatId());

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText("Бот не поддерживает команды от пользователя в данный момент");
            try {
                sendMessage(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessages() throws TelegramApiException {
        for (Long chatId : BotParams.userToChat) {
            synchronized (Thread.currentThread()) {
                try {
                    Thread.currentThread().wait(35);
                } catch (InterruptedException e) {
                    BotLogger.severe(LOGTAG, e);
                }
            }
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(chatId)
                    .setText(OscarService.getInstance().fetchDiscount(chatId));
            System.out.println("Sending Message to chat id " + chatId);
            sendMessage(message);
        }
    }

    @Override
    public String getBotUsername() {
        return BotParams.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BotParams.BOT_TOKEN;
    }
}
