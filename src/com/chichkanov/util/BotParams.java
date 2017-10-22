package com.chichkanov.util;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by chichkanov on 20/10/2017.
 * telegram - @chichkanov777
 */
public class BotParams {

    public static final int UPDATE_INTERVAL_SECONDS = 10;

    public static final String BOT_TOKEN = "452246408:AAHs8pxqvWbUGAh3Wfg94S2KSL2ZVSHQNF0";

    public static final String BOT_USERNAME = "Oscar";

    public static Set<Long> userToChat = new ConcurrentSkipListSet<>();

}
