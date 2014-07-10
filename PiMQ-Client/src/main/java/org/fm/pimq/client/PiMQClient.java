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
import org.fm.pimq.client.states.GPIOStatesProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that implements the client-side logic to manage JMS input.
 * It starts the main loop that reads the GPIO command from a JMS queue and sends
 * GPIO event to another JMS queue
 *
 * @author Fabio Marini
 */
public class PiMQClient {

    protected static final String DEFAULT_CONNECTION_STRING = "tcp://localhost:61616";

    protected static  final String DEFAULT_COMMAND_QUEUE_IDENTIFIER = "PiMQ.GPIO.Commands";

    protected static  final String DEFAULT_STATES_QUEUE_IDENTIFIER = "PiMQ.GPIO.States";

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

        String connectionUrl = DEFAULT_CONNECTION_STRING;
        if(args.length > 1){
            connectionUrl = args[1];
        }

        String commandsQueueName = DEFAULT_COMMAND_QUEUE_IDENTIFIER;
        if(args.length > 2){
            commandsQueueName = args[2];
        }

        String statusQueueName = DEFAULT_STATES_QUEUE_IDENTIFIER;
        if(args.length > 3){
            statusQueueName = args[3];
        }

        thread(new GPIOCommandsConsumer(connectionUrl, commandsQueueName), false);

        thread(new GPIOStatesProducer(connectionUrl, statusQueueName), false);
    }

    /**
     * Create a new thread the the given runnnable instance
     * @param runnable the instance to run
     * @param daemon true if will be a system deamon
     */
    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }


}
