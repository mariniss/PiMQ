/**
 * Copyright 2014 Fabio Marini
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fm.pimq.client;

import org.fm.pimq.client.commands.GPIOCommandsConsumer;
import org.fm.pimq.client.w1.W1BusRequestsConsumer;
import org.fm.pimq.conf.Configuration;
import org.fm.pimq.conf.ConfigurationProvider;
import org.fm.pimq.client.states.GPIOStatesProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class that implements the client-side logic to manage JMS input.
 * It starts the main loop that reads the GPIO command from a JMS queue and sends
 * GPIO event to another JMS queue
 *
 * @author Fabio Marini
 */
public class PiMQClient {

    /**
     * The default logger
     */
    private static final Logger logger = LoggerFactory.getLogger(PiMQClient.class);

    /**
     * Main method <br />
     *
     * It starts a new thread that contains the main loop to read JMS messages
     *
     * @param args program arguments
     * @throws Exception for all kinds of errors
     */
    public static void main(String[] args) throws Exception {
        logger.info("Starting PiMQClient");

        String configDirectoryPath = null;
        String configFileName = null;

        if(args != null || args.length == 2)
        {
            configDirectoryPath = args[0];
            configFileName = args[1];
        }
        else {
            logger.warn("Program's arguments not found, using default configuration");
        }

        Configuration configuration = ConfigurationProvider.getInstance().getConfiguration(configDirectoryPath, configFileName);
        logger.debug("Loaded configuration: {}", configuration);

        if(configuration.isEnableCommandsMessages()) {
            logger.info("starting commands consumer main loop...");

            thread(new GPIOCommandsConsumer(configuration), false);
        }

        if(configuration.isEnableStatesMessages()) {
            logger.info("starting status produce main loop...");

            thread(new GPIOStatesProducer(configuration), false);
        }

        if(configuration.isEnableW1BusMessages()) {
            logger.info("Staring w1 bus consumer main loop...");

            thread(new W1BusRequestsConsumer(configuration), false);
        }
    }

    /**
     * Create a new thread with the given runnnable instance
     * @param runnable the instance to run
     * @param daemon true if will be a system deamon
     */
    public static void thread(Runnable runnable, boolean daemon) {
        Thread processThread = new Thread(runnable);
        processThread.setDaemon(daemon);
        processThread.start();
    }

    /**
     * Create a new thread with the given runnnable instance to schedule every specified amount of seconds
     * @param runnable the instance to run
     * @param daemon true if will be a system deamon
     * @param delay the second to wait before start the thread
     * @param seconds the second rate every the thread is executed
     */
    public static void scheduledThread(Runnable runnable, boolean daemon, Long delay, Long seconds) {
        Thread processThread = new Thread(runnable);
        processThread.setDaemon(daemon);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(processThread, delay, seconds, TimeUnit.SECONDS);
    }
}
