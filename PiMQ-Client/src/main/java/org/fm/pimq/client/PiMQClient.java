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

import com.pi4j.io.gpio.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.fm.pimq.IPinCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.beans.ExceptionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Class with main method.
 * It starts the main loop
 * @author Fabio Marini
 */
public class PiMQClient {

    private static final Logger logger = LoggerFactory.getLogger(PiMQClient.class);

    private static final GpioController gpio = GpioFactory.getInstance();

    private static Map<Pin, GpioPinDigitalOutput> pinDigitalOutputMap = new HashMap<>();

    /**
     * Main method
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        thread(new GPIOMessagesConsumer(), false);
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class GPIOMessagesConsumer implements Runnable, ExceptionListener {
        public void run() {
            try {

                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.1:61616");

                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                //connection.setExceptionListener(this);

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("GPIO.Commands");

                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);

                logger.info(" Starting main loop ");

                for (; ; ) {

                    // Wait for a message
                    Message message = consumer.receive();

                    if (((ActiveMQObjectMessage) message).getObject() instanceof IPinCommand) {
                        IPinCommand commandMessage = (IPinCommand) ((ActiveMQObjectMessage) message).getObject();

                        executeCommand(commandMessage);

                        logger.debug("Received: " + commandMessage.getPin().getPinNumber() + " : " + commandMessage.getState().name());
                    } else {
                        break;
                    }
                }

                consumer.close();
                session.close();
                connection.close();

            } catch (Exception e) {
                logger.error("Caught: " + e);
            }
        }

        /**
         * @param commandMessage
         */
        private void executeCommand(IPinCommand commandMessage) {
            Pin pipin = null;

            switch (commandMessage.getPin().getPinNumber()) {
                case 1:
                    pipin = RaspiPin.GPIO_01;
                    break;
                //TODO:
            }

            GpioPinDigitalOutput pinOut  = pinDigitalOutputMap.get(pipin);
            if(pinOut == null) {
                pinOut = gpio.provisionDigitalOutputPin(pipin, "GPIO", PinState.LOW);
                pinDigitalOutputMap.put(pipin, pinOut);
            }

            switch (commandMessage.getState()) {
                case LOW:
                    pinOut.low();
                    break;

                case HIGH:
                    pinOut.high();
                    break;

                default:

            }
        }

        public void exceptionThrown(Exception e) {
            logger.error("JMS Exception occured.  Shutting down client.");
        }
    }
}
