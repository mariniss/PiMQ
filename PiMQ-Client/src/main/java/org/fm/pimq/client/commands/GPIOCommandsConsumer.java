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
package org.fm.pimq.client.commands;

import org.apache.activemq.command.ActiveMQObjectMessage;

import com.pi4j.io.gpio.*;
import org.fm.pimq.IPinMessage;
import org.fm.pimq.conf.Configuration;
import org.fm.pimq.net.ConnectionProvidersRepository;
import org.fm.pimq.net.IConnectionProviderStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The JMS consumer for the GPIO commands. <br/>
 * Executes all command that reads from JMS queue
 *
 * @author Fabio Marini
 */
public class GPIOCommandsConsumer implements Runnable, ExceptionListener {
    /**
     * The default logger
     */
    private static final Logger logger = LoggerFactory.getLogger(GPIOCommandsConsumer.class);

    /**
     * GPIO controller
     */
    protected  GpioController gpioController;

    /**
     * Map of pin -> Digital output manager
     */
    protected Map<Pin, GpioPinDigitalOutput> pinDigitalOutputMap = new HashMap<>();

    /**
     *  Configuration instance
     */
    protected Configuration configuration;

    /**
     * Build the consumer with the given attributes
     * @param configuration configuration object with all information necessary to connect to JMS server
     * @param instanceForTest flags that indicate if the created instance is used only to unit testing
     */
    public GPIOCommandsConsumer(Configuration configuration, boolean instanceForTest) {
        this.configuration = configuration;

        if(!instanceForTest) {
            gpioController = GpioFactory.getInstance();
        }
    }

    /**
     * Build the consumer with the given attributes
     * @param configuration configuration object with all information necessary to connect to JMS server
     */
    public GPIOCommandsConsumer(Configuration configuration) {
        this(configuration, false);
    }

    @Override
    public void run() {
        try {
            if(configuration == null){
                throw new IllegalArgumentException("Configuration cannot be null");
            }

            IConnectionProviderStrategy connectionProvider = ConnectionProvidersRepository.getConnectionStrategy(configuration.getServerType());
            Connection connection = connectionProvider.createConnection(configuration);

            connection.start();
            connection.setExceptionListener(this);

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue(configuration.getCommandsQueueName());

            MessageConsumer consumer = session.createConsumer(destination);

            logger.info(" Starting commands consumer main loop ");

            for ( ; ; ) {
                Message message = consumer.receive();

                if (((ActiveMQObjectMessage) message).getObject() instanceof IPinMessage) {
                    IPinMessage commandMessage = (IPinMessage) ((ActiveMQObjectMessage) message).getObject();

                    StringBuilder errorMsg = new StringBuilder();
                    if (isValidCommand(commandMessage, errorMsg)) {
                        logger.debug("Received command for Pin: " + commandMessage.getPin().getPinNumber() + " - State: " + commandMessage.getState().name());

                        executeCommand(commandMessage);
                    } else {
                        logger.error("Received invalid command : " + errorMsg.toString());
                    }

                } else {
                    logger.error("Received JMS messages that is not a IPinMessage. Main loop ended");
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
     * Checks if the received message can be handled or not
     *
     * @param commandMessage the command message to check
     * @param errorMsg       the error message, if is not valid
     * @return true if the message is valid, false otherwise
     */
    private boolean isValidCommand(IPinMessage commandMessage, StringBuilder errorMsg) {
        if (commandMessage == null) {
            errorMsg.append("Null command");
            return false;
        }

        if (commandMessage.getPin() == null) {
            errorMsg.append("Null pin");
            return false;
        }

        if (commandMessage.getPin().getPinNumber() == null) {
            errorMsg.append("Null pin number");
            return false;
        }

        if (commandMessage.getPin().getPinNumber() < 0 || commandMessage.getPin().getPinNumber() > 20) {
            errorMsg.append("Pin number must be from 1 to 20");
            return false;
        }

        if (commandMessage.getState() == null) {
            errorMsg.append("Null pin state");
            return false;
        }

        return true;
    }

    /**
     * Execute the given command to Raspberry Pi
     *
     * @param commandMessage the command to execute
     */
    private synchronized void executeCommand(IPinMessage commandMessage) {
        Pin pipin;

        switch (commandMessage.getPin().getPinNumber()) {
            case 0:
                pipin = RaspiPin.GPIO_00;
                break;
            case 1:
                pipin = RaspiPin.GPIO_01;
                break;
            case 2:
                pipin = RaspiPin.GPIO_02;
                break;
            case 3:
                pipin = RaspiPin.GPIO_03;
                break;
            case 4:
                pipin = RaspiPin.GPIO_04;
                break;
            case 5:
                pipin = RaspiPin.GPIO_05;
                break;
            case 6:
                pipin = RaspiPin.GPIO_06;
                break;
            case 7:
                pipin = RaspiPin.GPIO_07;
                break;
            case 8:
                pipin = RaspiPin.GPIO_08;
                break;
            case 9:
                pipin = RaspiPin.GPIO_09;
                break;
            case 10:
                pipin = RaspiPin.GPIO_10;
                break;
            case 11:
                pipin = RaspiPin.GPIO_11;
                break;
            case 12:
                pipin = RaspiPin.GPIO_12;
                break;
            case 13:
                pipin = RaspiPin.GPIO_13;
                break;
            case 14:
                pipin = RaspiPin.GPIO_14;
                break;
            case 15:
                pipin = RaspiPin.GPIO_15;
                break;
            case 16:
                pipin = RaspiPin.GPIO_16;
                break;
            case 17:
                pipin = RaspiPin.GPIO_17;
                break;
            case 18:
                pipin = RaspiPin.GPIO_18;
                break;
            case 19:
                pipin = RaspiPin.GPIO_19;
                break;
            case 20:
                pipin = RaspiPin.GPIO_20;
                break;
            default:
                pipin = null;
                break;
        }

        if (pipin != null) {
            GpioPinDigitalOutput pinOut = pinDigitalOutputMap.get(pipin);
            if (pinOut == null) {
                pinOut = gpioController.provisionDigitalOutputPin(pipin, "GPIO", PinState.LOW);
                pinDigitalOutputMap.put(pipin, pinOut);
            }


            if (pinOut != null) {
                switch (commandMessage.getState()) {
                    case LOW:
                        pinOut.low();
                        break;

                    case HIGH:
                        pinOut.high();
                        break;

                    default:

                }
            } else {
                logger.warn("No GpioPinDigitalOutput found the the pin " + pipin.getName());
            }
        } else {
            logger.warn("No RaspiPin found for the pin number " + commandMessage.getPin().getPinNumber());
        }

    }

    /** {@inheritDoc} */
    @Override
    public void onException(JMSException exception) {
        logger.error("JMS Exception occurred.  Shutting down client.");
        System.out.print("JMS Exception occurred.  Shutting down client.");
        exception.printStackTrace();
    }
}