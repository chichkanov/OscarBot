package com.chichkanov;

import com.chichkanov.service.OscarService;
import com.chichkanov.service.TimerExecutor;
import com.chichkanov.service.UpdateTask;
import com.chichkanov.util.BotParams;
import com.chichkanov.util.Commands;
import com.chichkanov.util.JsonData;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
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
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText() || message.hasLocation()) {
                handleIncomingMessage(message);
            }
        }
    }

    private void handleIncomingMessage(Message message) {
        if (message.getText().startsWith(Commands.commandInitChar)) {
            String messageText = message.getText();

            SendMessage answer = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(message.getChatId());

            String answerText = "Бот не поддерживает данную команду от пользователя";

            String command = messageText.substring(messageText.indexOf('/'), messageText.length());
            switch (command) {
                case Commands.buyStudentPack: {
                    BotLogger.severe(LOGTAG, "Buying student pack");
                    OscarService.getInstance().buyPack(message.getChatId(), JsonData.STUDENT_PACK_JSON);
                    answerText = "Вы купили студенческий пак";
                    break;
                }
                case Commands.buyBuilderPack: {
                    BotLogger.severe(LOGTAG, "Buying builder pack");
                    OscarService.getInstance().buyPack(message.getChatId(), JsonData.BUILDER_PACK_JSON);
                    answerText = "Вы купили бодибилдерский пак";
                    break;
                }
                case Commands.buyWomanPack: {
                    BotLogger.severe(LOGTAG, "Buying woman pack");
                    OscarService.getInstance().buyPack(message.getChatId(), JsonData.WOMAN_PACK_JSON);
                    answerText = "Вы купили женский пак";
                    break;
                }
            }
            try {
                sendMessage(answer.setText(answerText));
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
